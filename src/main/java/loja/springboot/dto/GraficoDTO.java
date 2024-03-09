package loja.springboot.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
@Immutable
@Subselect("Select *, 1 as grafico from  vw_receita_despesas")
@Cacheable

public class GraficoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "meses")
    private Long mes;
	@Column(name = "despesas")
    private Double despesa;
	@Column(name = "entradas")
    private Double entrada;
	
	@Transient 
	List<String> meses = new ArrayList<String>();
	
	@Transient 
	List<Double> entradas = new ArrayList<Double>();

	public Long getMes() {
		return mes;
	}

	public void setMes(Long mes) {
		this.mes = mes;
	}

	public Double getDespesa() {
		return despesa;
	}

	public void setDespesa(Double despesa) {
		this.despesa = despesa;
	}

	public Double getEntrada() {
		return entrada;
	}

	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}

	public List<String> getMeses() {
		return meses;
	}

	public void setMeses(List<String> meses) {
		this.meses = meses;
	}

	public List<Double> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Double> entradas) {
		this.entradas = entradas;
	}

	
}
