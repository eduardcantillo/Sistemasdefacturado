package com.bolsadeideas.spring.horario.datajpa.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
import com.bolsadeideas.spring.horario.datajpa.app.models.Factura;
import com.bolsadeideas.spring.horario.datajpa.app.models.Producto;

public interface IClienteService {

	public List<Cliente> findAll();
	public Page<Cliente> findAll(Pageable pageable);
	public void save(Cliente cliente);
	public Cliente getById(Long id);
	public void delete(Long id);
	public List<Producto> findByName(String term);
	public void saveFactura(Factura factura);
	public Producto findProductoById(Integer id);
	public Factura findFacturaById(Long id);
	public void deleteFactura(Long id);
	public Factura fetchFacturaByid(Long id);
	public Cliente fetchClienteWithFactura(Long id);
}
