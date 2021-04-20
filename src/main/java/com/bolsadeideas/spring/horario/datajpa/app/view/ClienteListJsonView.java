package com.bolsadeideas.spring.horario.datajpa.app.view;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		
		model.remove("titulo");
		model.remove("clientes");
		model.remove("page");
		List<Cliente> clientes=(List<Cliente>)model.get("cl");
		return clientes;
	}

	
}
