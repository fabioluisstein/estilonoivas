package loja.springboot.model;
import java.io.Serializable;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tempo_operacao")
public class TempoOperacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String complexidade;
    private Time tempo;


	@ManyToOne(fetch = FetchType.LAZY )
	@JsonIgnore
    @JoinColumn(name = "categoria_id_produto")
	private Categoria tipoProduto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComplexidade() {
		return complexidade;
	}

	public Time getTempo() {
		return tempo;
	}

	public void setTempo(Time tempo) {
		this.tempo = tempo;
	}

	public void setComplexidade(String complexidade) {
		this.complexidade = complexidade;
	}

	public Categoria getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(Categoria tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	@Override
	public String toString() {
		return "TempoOperacao [id=" + id + "]";
	}
	





	
}
