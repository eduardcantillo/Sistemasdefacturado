package com.bolsadeideas.spring.horario.datajpa.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.spring.horario.datajpa.app.dao.IClienteDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.IFacturaDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.IProductoDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Cliente;
import com.bolsadeideas.spring.horario.datajpa.app.models.Factura;
import com.bolsadeideas.spring.horario.datajpa.app.models.Producto;

@Service
public class ClienteServiceImplement implements IClienteService {

	@Autowired
	IClienteDao clienteDao;
	@Autowired
	IProductoDao productoDao;
	@Autowired
	IFacturaDao facturaDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		return  (List<Cliente>) clienteDao.findAll();
	}

	@Transactional
	@Override
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Cliente getById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByName(String term) {
		// TODO Auto-generated method stub
		return productoDao.findByName(term);
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		this.facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Integer id) {
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		
		return this.facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		this.facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByid(Long id) {
		return this.facturaDao.fetchByIdWithClienteWithItemsAndProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchClienteWithFactura(Long id) {
		
		return this.clienteDao.fetchByIdWithFactura(id);
	}

}
