package com.bolsadeideas.spring.horario.datajpa.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
import com.bolsadeideas.spring.horario.datajpa.app.models.Factura;
import com.bolsadeideas.spring.horario.datajpa.app.models.ItemFactura;
import com.bolsadeideas.spring.horario.datajpa.app.models.Producto;
import com.bolsadeideas.spring.horario.datajpa.app.service.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	@Autowired
	IClienteService cliente;
	
	@GetMapping(value = "/form/{clienteId}")
	public String crear(@PathVariable Long clienteId, Model model,RedirectAttributes flash) {
		Cliente cliente=this.cliente.getById(clienteId);
		
		if(cliente==null) {
			flash.addAttribute("error","el cliente no existe en la bd");
			return "redirect:/listar";
		}
		Factura factura=new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "crearFactura");
		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}",produces = {"application/json"})
	public @ResponseBody  List<Producto> cargarProductos(@PathVariable String term) {
		
		return cliente.findByName(term);
	}
	@GetMapping(value="/ver/{id}")
	public String verFactura(@PathVariable Long id,Model model,RedirectAttributes flash) {
		Factura factura=cliente.fetchFacturaByid(id);//cliente.findFacturaById(id);
		if(factura==null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
		model.addAttribute("factura", factura);
		
		return "factura/ver";
	}
	
	@PostMapping(value="/form")
	public String guardarFactura(@Valid Factura factura,BindingResult result,Model model,@RequestParam(name="item_id[]",required=false) Integer[] itemId,
			@RequestParam(name="cantidad[]",required=false) Integer[] cantidad,SessionStatus status,RedirectAttributes flash) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo","crear Factura");
			return "factura/form";
		}
		
		if(itemId == null || itemId.length==0) {
			model.addAttribute("titulo","crear Factura");
			model.addAttribute("error","La factura NO puede no tener linea!");
			return "factura/form";
		}
		for(int i=0;i<itemId.length;i++) {
			Producto producto=cliente.findProductoById(itemId[i]);
			ItemFactura linea=new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItems(linea);
		}
		
		cliente.saveFactura(factura);
		
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con exito!");
		return "redirect:/ver/"+factura.getCliente().getId();
	}
	
	@GetMapping(value = "/eliminar/{id}")
	public String eliminarFactura(@PathVariable Long id,RedirectAttributes flash) {
		Factura factura=this.cliente.findFacturaById(id);
		if(factura==null) {
			
			flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar");
			return "redirect:/listar";
		}
		this.cliente.deleteFactura(id);
		flash.addFlashAttribute("success", "Factura eliminada");
		return "redirect:/ver/"+factura.getCliente().getId();
	}
	
}
