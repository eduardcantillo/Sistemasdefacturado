package com.bolsadeideas.spring.horario.datajpa.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	
	@GetMapping(value="/login")
	public String login(@RequestParam(required=false) String error,@RequestParam(required=false) String logout,Model model,Principal principal,RedirectAttributes flash) {
		
		if(principal!=null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesion previamente");
			return"redirect:/";
		}
		
		if(error!=null) {
			model.addAttribute("error", "Error al iniciar sesion nombre de usuario o cantraseña incorrecta");
		}
		
		if(logout!=null) {
			model.addAttribute("info", "Se ha cerrado la sescion correctamente");
		}
		
		return "login";
	}
	
}
