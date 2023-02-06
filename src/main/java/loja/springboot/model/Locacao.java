package loja.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Cacheable
@Table(name="locacao")
public class Locacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String observacao;
	@ManyToOne
	private Cliente cliente;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_locacao;
		
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_evento;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_retirada;
	
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_devolucao;
			
	@ManyToOne
	private Pessoa colaborador;
	
	@OneToMany(mappedBy = "locacao")
	List<Parcela> parcelas;	
	
	@OneToMany(mappedBy = "locacao")
	List<LocacaoProduto> produtos;	
	
	private String origem;  
	
	private String papel;

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Parcela> getParcelas() {
		return parcelas;
	}

	

	public double getValorTotal() {
		Double valor = 0.00;
		for(Parcela parcela : getParcelas()) {  
		   if(parcela.getData_pagamento()!=null) {
			 valor = valor +	parcela.getValor();
		   }
		
		}  
		return valor;
	}
	
	
	public double getValorTotalProdutos() {
		Double valor = 0.00;
		for(LocacaoProduto produtosLocao : getProdutos()) {  
			valor = valor +	produtosLocao.getValor();
		}  
		return valor;
	}
	
	public double getDiferencaPagar() {
		Double valor = 0.00;
		valor = getValorTotalProdutos() - getValorTotal();
		return valor;
	}
	
	

	public List<LocacaoProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<LocacaoProduto> produtos) {
		this.produtos = produtos;
	}

	public void setParcelas(List<Parcela> parcelas) {
		this.parcelas = parcelas;
	}

	public String getObservacao() {
		return observacao;
	}



	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}



	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}



	public Date getData_locacao() {
		return data_locacao;
	}



	public void setData_locacao(Date data_locacao) {
		this.data_locacao = data_locacao;
	}



	public Date getData_evento() {
		return data_evento;
	}



	public void setData_evento(Date data_evento) {
		this.data_evento = data_evento;
	}



	public Date getData_retirada() {
		return data_retirada;
	}



	public void setData_retirada(Date data_retirada) {
		this.data_retirada = data_retirada;
	}



	public Date getData_devolucao() {
		return data_devolucao;
	}



	public void setData_devolucao(Date data_devolucao) {
		this.data_devolucao = data_devolucao;
	}

	public Pessoa getColaborador() {
		return colaborador;
	}



	public void setColaborador(Pessoa colaborador) {
		this.colaborador = colaborador;
	}



	@Override
	public String toString() {
		return id.toString();
	}

}
