package loja.springboot.dto;


import java.io.Serializable;
import java.util.Date;



public class dtoProdutoPesquisa implements Serializable {


	private static final long serialVersionUID = 1L;
	private Long id;
	private String descricao;
	private Double valorCompra;
	private Double valorVenda;

	private String cor;  
	private String tamanho;
	private Date data_compra;
		
	private Integer quantidade_acesso;
	

	private Integer quantidadeEstoque;
	
	 
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public Double getValorCompra() {
		return valorCompra;
	}



	public void setValorCompra(Double valorCompra) {
		this.valorCompra = valorCompra;
	}



	public Double getValorVenda() {
		return valorVenda;
	}



	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}



	public String getCor() {
		return cor;
	}



	public void setCor(String cor) {
		this.cor = cor;
	}



	public String getTamanho() {
		return tamanho;
	}



	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}



	public Date getData_compra() {
		return data_compra;
	}



	public void setData_compra(Date data_compra) {
		this.data_compra = data_compra;
	}



	public Integer getQuantidade_acesso() {
		return quantidade_acesso;
	}



	public void setQuantidade_acesso(Integer quantidade_acesso) {
		this.quantidade_acesso = quantidade_acesso;
	}



	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}



	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}



	@Override
	public String toString() {
		return descricao;
	}

}
