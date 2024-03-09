package loja.springboot.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.TransactionScoped;

@Entity
@Table(name = "cidade")
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado_id")
	private Estado estado;

	@OneToMany(mappedBy = "cidade")
	private List<Cliente> locacoes;

	@OneToMany(mappedBy = "cidade_evento")
	private List<Locacao> eventosCidades;

	@OneToMany(mappedBy = "cidade")
	private List<Fornecedor> fornecedores;
	
	@OneToMany(mappedBy = "cidade")
	private List<Cliente> clientes;

	@TransactionScoped
	private Long quantidadeCliente;

	public Cidade() {
	}

	public Cidade(Long id) {
		this.id = id;
	}

	public List<Locacao> getEventosCidades() {
		return eventosCidades;
	}

	public void setEventosCidades(List<Locacao> eventosCidades) {
		this.eventosCidades = eventosCidades;
	}

	public Cidade(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<Cliente> getLocacoes() {
		return locacoes;
	}

	public void setLocacoes(List<Cliente> locacoes) {
		this.locacoes = locacoes;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
