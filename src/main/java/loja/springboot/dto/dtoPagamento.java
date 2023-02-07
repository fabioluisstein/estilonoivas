package loja.springboot.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
@Cacheable

public class dtoPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String tipo;
	private Date data;
	private String data_param;
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

	public String getData_param() {
		return data_param;
	}

	public void setData_param(String data_param) {
		this.data_param = data_param;
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
