package com.bolsadeideas.spring.horario.datajpa.app.service;

import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.spring.horario.datajpa.app.dao.IUsuarioDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.models.Role;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private IUsuarioDao user;
	
	
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario=user.findUserByUsername(username);
		
		if(usuario==null) {
			System.err.println("error: el usuario :"+username+" no existe en la bd");
			throw new UsernameNotFoundException("El usuario no existe");
		}
		List <GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		
		
		
		for(Role rol: usuario.getRoles()) {
			System.out.println("Rol: "+rol.getAuthority());
			authorities.add(new SimpleGrantedAuthority(rol.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			System.err.println("error: en el login :"+username+" no tiene ningun rol asignado");
			throw new UsernameNotFoundException("Rol no existe");
		}
		
		return new User(username, usuario.getPassword(),usuario.getEnable(), true, true, true, authorities);
	}

}
