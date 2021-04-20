package com.bolsadeideas.spring.horario.datajpa.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {
	
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String url=request.getHeader("referer");
		
		return "redirect:".concat(url);
	}

}
