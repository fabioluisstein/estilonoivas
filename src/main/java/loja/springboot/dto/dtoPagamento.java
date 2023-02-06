package loja.springboot.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Immutable
@Subselect("SELECT * FROM vw_pagamentos order by id desc")
@Cacheable

public class dtoPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String tipo;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data;
	private String fornecedor;  
	private String moeda;  
	private String origem;
	private Double valor;
	private String anexo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getData() {
		return data;
	}

	public String getAnexo() {
		return anexo;
	}

	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

}
