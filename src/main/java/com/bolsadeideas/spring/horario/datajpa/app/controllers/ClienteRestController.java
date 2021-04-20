package com.bolsadeideas.spring.horario.datajpa.app.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.bolsadeideas.spring.horario.datajpa.app.service.IClienteService;
import com.bolsadeideas.spring.horario.datajpa.app.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService cliente;
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public  ClienteList listarRest() {
		return new ClienteList(cliente.findAll());
	}
}
