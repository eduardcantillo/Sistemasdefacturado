package com.bolsadeideas.spring.horario.datajpa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.spring.horario.datajpa.app.service.IUploadService;

@SpringBootApplication
public class SpringBootJpaApplication implements CommandLineRunner{

	@Autowired
	private IUploadService service;
	
	@Autowired
	BCryptPasswordEncoder passwordEncode;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.deleteAll();
		service.init();
		
		String password="12345";
		
		for(int i =0;i<2;i++) {
			String bcryptPassword=passwordEncode.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}

}
