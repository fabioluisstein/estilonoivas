package loja.springboot.model;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="empresa") 
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cnpj;
	private String dominio;
	private String cidade;
	private double valorMedioDia;
	private String telefone;
	private Double investimentoInicial;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_inicio;

	public Double getInvestimentoInicial() {
		return investimentoInicial;
	}
	public void setInvestimentoInicial(Double investimentoInicial) {
		this.investimentoInicial = investimentoInicial;
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
	public String getCnpj() {
		return cnpj;
	}
	
	public double getValorMedioDia() {
		return valorMedioDia;
	}
	public void setValorMedioDia(double valorMedioDia) {
		this.valorMedioDia = valorMedioDia;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getCidade() {
		return cidade;
	}	
	public Date getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	
}
