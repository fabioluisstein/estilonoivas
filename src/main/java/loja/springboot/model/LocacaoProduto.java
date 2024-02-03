package loja.springboot.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="locacaoProduto")
public class LocacaoProduto implements Serializable {
 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Double valor;
	private String observacao;
		
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "idlocacao", nullable = false)
	private Locacao locacao;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_liberacao;
	
	@CreationTimestamp	
	@Column(name = "data_hora", nullable = true)
	private LocalDateTime data_hora;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JsonIgnore
    @JoinColumn(name = "produto_id")
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JsonIgnore
	@JoinColumn(name = "id_ajuste", nullable = true)
	private Categoria categoriaAjuste;
	

	private String nomeArquivo;
	
	private String tipoArquivo;
	
	@Lob
	private byte[] arquivo;
	

    public LocalDateTime getData_hora() {
		return data_hora;
	}

	public void setData_hora(LocalDateTime data_hora) {
		this.data_hora = data_hora;
	}

	public LocacaoProduto (){

	}

	public LocacaoProduto (Locacao locacao){
		this.locacao = locacao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	
	public String getNomeArquivo() {
		return nomeArquivo;
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

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Categoria getCategoriaAjuste() {
		return categoriaAjuste;
	}

	public void setCategoriaAjuste(Categoria categoriaAjuste) {
		this.categoriaAjuste = categoriaAjuste;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}


	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Locacao getLocacao() {
		return locacao;
	}


	public void setLocacao(Locacao locacao) {
		this.locacao = locacao;
	}

	public Date getData_liberacao() {
		return data_liberacao;
	}


	public void setData_liberacao(Date data_liberacao) {
		this.data_liberacao = data_liberacao;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
