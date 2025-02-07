package loja.springboot.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="locacao")
public class Locacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String observacao;
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_locacao;
		
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_evento;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_retirada;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_prova;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_devolucao;
			
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "colaborador_id")
	private Pessoa colaborador;
	
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cidade_evento_id")
	private Cidade cidade_evento;

	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "evento_id")
	private Categoria evento;

	private String detalhes_evento;

	@OneToMany(mappedBy = "locacao")
	List<Parcela> parcelas = new ArrayList<Parcela>(); 	
	
	@OneToMany(mappedBy = "locacao")
	List<LocacaoProduto> produtos = new ArrayList<LocacaoProduto>(); 	
	
	private String origem;  
	
	private String papel;

	private Integer emailEnviado;

	public Locacao(){

	}

	public Locacao (Cliente cliente){
		this.cliente = cliente;
	}

	public Cidade getCidade_evento() {
		return cidade_evento;
	}

	

	public Integer getEmailEnviado() {
		return emailEnviado;
	}

	public void setEmailEnviado(Integer emailEnviado) {
		this.emailEnviado = emailEnviado;
	}

	public void setCidade_evento(Cidade cidade_evento) {
		this.cidade_evento = cidade_evento;
	}

	public Categoria getEvento() {
		return evento;
	}

	public void setEvento(Categoria evento) {
		this.evento = evento;
	}

	public String getDetalhes_evento() {
		return detalhes_evento;
	}

	public void setDetalhes_evento(String detalhes_evento) {
		this.detalhes_evento = detalhes_evento;
	}

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


	public Date getData_prova() {
		return data_prova;
	}

	public void setData_prova(Date data_prova) {
		this.data_prova = data_prova;
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
