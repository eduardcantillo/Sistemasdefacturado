package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name="facturas")
public class Factura implements Serializable {

	/**
	 * 
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "el campo descripcion es requerido")
	private String descripcion;
	private String observacion;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createAt;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="factura_id")
	List <ItemFactura> items;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	private Cliente cliente;
	
	@PrePersist
	public void prePersist() {
		this.createAt=new Date();
	}
	
	
	public Factura() {
		items=new ArrayList<>();
	}




	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}




	public Factura(Long id, String descripcion, String observacion, Date createAt, Cliente cliente) {
		
		this.id = id;
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.createAt = createAt;
		this.cliente = cliente;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getDescripcion() {
		return descripcion;
	}




	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}




	public String getObservacion() {
		return observacion;
	}




	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}




	public Date getCreateAt() {
		return createAt;
	}




	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	} 	

	@XmlTransient
	public Cliente getCliente() {
		return cliente;
	}




	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void addItems(ItemFactura item) {
		
		if(item!=null) {
			items.add(item);
		}
	}

	public double getTotal() {
		double total=0.0;
		
		for(ItemFactura i: items){
			if(i!=null) {
			total+=i.calcularImporte();
			}
		}
		
		
		return total;
	}
	
	private static final long serialVersionUID = 1L;
}
