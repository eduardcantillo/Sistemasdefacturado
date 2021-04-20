package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.username=?1")
	public Usuario findUserByUsername(String username);
}
