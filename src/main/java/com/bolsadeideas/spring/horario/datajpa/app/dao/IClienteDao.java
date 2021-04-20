package com.bolsadeideas.spring.horario.datajpa.app.dao;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long>{

	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFactura(Long id);
}
