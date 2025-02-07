package loja.springboot.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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
@Table(name="produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	private Double valorCompra;
	private Double valorVenda;
	private String cor;  
	private String tamanho;
	private Long imprimir;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_compra;
	private Integer quantidade_acesso;
	private Integer quantidadeEstoque;
	private String nomeArquivo;
	private String tipoArquivo;
	
	
	private byte[] arquivo;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JsonIgnore
    @JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JsonIgnore
    @JoinColumn(name = "fornecedor_id")
	private Fornecedor fornecedor;
	
	@OneToMany(mappedBy = "produto")
    private List<LocacaoProduto> produtosLocacoes;

	
	public Long getImprimir() {
		return imprimir;
	}

	public void setImprimir(Long imprimir) {
		this.imprimir = imprimir;
	}

	@Column(name = "total_likes", nullable = true)
	private Integer likes;

	public List<LocacaoProduto> getProdutosLocacoes() {
		return produtosLocacoes;
	}

	public void setProdutosLocacoes(List<LocacaoProduto> produtosLocacoes) {
		this.produtosLocacoes = produtosLocacoes;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public byte[] getArquivo() {
		return arquivo;
	}



	public String generateBase64Image()
	{

		return Base64.encodeBase64String(this.getArquivo());
	}
	

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
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

	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
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
		if(quantidade_acesso==null)
		return 0;
		else
		return quantidade_acesso;
	}

	public void setQuantidade_acesso(Integer quantidade_acesso) {
		this.quantidade_acesso = quantidade_acesso;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	@Override
	public String toString() {
		return descricao;
	}

}
