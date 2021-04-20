package com.bolsadeideas.spring.horario.datajpa.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Producto;

public interface IProductoDao extends CrudRepository<Producto, Integer> {
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByName(String term);

}
