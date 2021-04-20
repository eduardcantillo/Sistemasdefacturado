package com.bolsadeideas.spring.horario.datajpa.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
import com.bolsadeideas.spring.horario.datajpa.app.xml.ClienteList;

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {

	
	@Autowired
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		model.remove("clientes");
		model.remove("page");
		model.remove("titulo");
		
		List <Cliente> clientes=(List<Cliente>) model.get("cl");
		model.remove("cl");
		model.put("clienteList",new ClienteList(clientes));
		
		super.renderMergedOutputModel(model, request, response);
	}
	
	

}
