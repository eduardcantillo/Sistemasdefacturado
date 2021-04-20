package com.bolsadeideas.spring.horario.datajpa.app.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
import com.bolsadeideas.spring.horario.datajpa.app.service.IClienteService;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUploadService;
import com.bolsadeideas.spring.horario.datajpa.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService cliente;

	@Autowired
	@Qualifier("messageSource")
	private MessageSource message;
	
	@Autowired
	private IUploadService upload;
	
	@RequestMapping(value = "/listar-rest", method = RequestMethod.GET)
	@ResponseBody
	public  List<Cliente> listarRest() {
		return cliente.findAll();
	}
	
	@RequestMapping(value = {"/listar","","/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
			Model model,Authentication authentication,HttpServletRequest request,Locale locale) {

		if(authentication!=null) {
			//Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		}
		
		if(hasRole("ROLE_ADMIN")) {
			System.out.println(authentication.getPrincipal());
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext=new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			System.out.println("Forma usando SecurityContextHolderAwareRequestWrapper: "+authentication.getPrincipal());
		}
		
		List<Cliente> cl=cliente.findAll();
		
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = cliente.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		System.out.println(message.getMessage("text.cliente.form.titulo", null, locale).toString());
		model.addAttribute("titulo", message.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("cl", cl);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/upload/{filename:.+}")
	public ResponseEntity<Resource> verfoto(@PathVariable String filename) {

		Resource resource=null;
		try {
			resource = this.upload.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@Secured("ROLE_USER")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {

		Cliente cli = cliente.fetchClienteWithFactura(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "el ecliente no existe en la bd");
			return "redirect:/listar";
		}

		model.addAttribute("cliente", cli);
		model.addAttribute("titulo", "Detalle del cliente: " + cli.getNombre());

		return "ver";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("titulo", "formulario de cliente");
		model.addAttribute("cliente", new Cliente());
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes flask) {
		Cliente cliente = this.cliente.getById(id);
		if (cliente == null) {
			flask.addFlashAttribute("error", "ha ocurrido un error inesperado el suario no existe en la bd");
			return "redirect:/listar";
		}

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "edicion de cliente");

		return "editar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flask) {
		Cliente cliente = this.cliente.getById(id);
		if (cliente != null) {

			this.cliente.delete(id);
			flask.addFlashAttribute("success", "cliente eliminado sastifactoriamente");

			if (!cliente.getFoto().isBlank()) {
				eliminarFoto(cliente);
			}
			}
		return "redirect:/listar";
	}

	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flask, Model model,
			@RequestParam(name = "file") MultipartFile foto,Locale locale) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", message.getMessage("text.cliente.form.titulo", null, locale));
			flask.addFlashAttribute("error", "Ha ocurrido un error inesperado");
			return "redirect:/form";
		}

		if (!foto.isEmpty()) {
			String uniqueFile=null;
			try {
				uniqueFile=this.upload.copy(foto);
				flask.addFlashAttribute("info", "Ha subido correctamente la foto '" + foto.getOriginalFilename() + "'");
				cliente.setFoto(uniqueFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		System.out.println(cliente);
		this.cliente.save(cliente);
		flask.addFlashAttribute("success", "cliente creado con exito");
		return "redirect:/listar";
	}

	public boolean eliminarFoto(Cliente cliente ) {
		
		if(cliente.getFoto().isBlank()) 
			return false;
		else 
			return upload.delete(cliente.getFoto());
	}
	
	@RequestMapping(value = "/guardarEdicion", method = RequestMethod.POST)
	public String guardarEdicion(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flask,
			@RequestParam(name = "file") MultipartFile foto,Locale locale) {

		if (result.hasErrors()) {
			
			model.addAttribute("titulo", message.getMessage("text.cliente.form.titulo", null, locale));

			return "editar";
		}
		
		
		if(!foto.isEmpty()) {
			try {
				eliminarFoto(cliente);
				String uniqueFile=this.upload.copy(foto);
				cliente.setFoto(uniqueFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			
		}
		this.cliente.save(cliente);
		flask.addFlashAttribute("success", "Cliente actualizado correctamente");
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context=SecurityContextHolder.getContext();
		if(context==null) {
			return false;
		}
		Authentication auth=context.getAuthentication();
		
		if(auth==null) {
			return false;
		}
		
		Collection <? extends GrantedAuthority> authorities=auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*for(GrantedAuthority authority :authorities) {
			
			if(role.equals(authority.getAuthority())) {
				System.out.println("Hola tu rol es:"+authority.getAuthority());
				return true;
			}
		}
		
		return false;*/
	}

}
