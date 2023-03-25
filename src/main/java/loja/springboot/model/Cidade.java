package loja.springboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.TransactionScoped;

import org.springframework.cache.annotation.Cacheable;

@Entity
@Cacheable
@Table(name="cidade")
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "estado_id")
	private Estado estado;
	  
  @TransactionScoped
	private Long quantidadeCliente; 

public Cidade(Long id){
	this.id = id;
}


public Cidade(Long id, String nome){
	this.id = id;
	this.nome = nome;
}


public Cidade(){

}

	public Long getQuantidadeCliente() {
		return quantidadeCliente;
	}

	public void setQuantidadeCliente(Long quantidadeCliente) {
		this.quantidadeCliente = quantidadeCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return nome;
	}

}
