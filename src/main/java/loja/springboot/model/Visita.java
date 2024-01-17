package loja.springboot.model;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Cacheable
@Table(name="visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nomeCliente;  

	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cidade_cliente")
	private Cidade cidadeCliente;
	
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cidade_evento")
	private Cidade cidadeEvento;

	private String tipoCliente;  
	private String motivo;  
	private String telefone;  
	private String origemContato;  
	private String evolucao;  
	private String facebook;  
	private String instagram;  
	private String retorno;  
	private String status;  
	private Long id_locacao;
	@Column(length = 13000)
	private String atendimento; 

	public Long getId_locacao() {
		return id_locacao;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public void setId_locacao(Long id_locacao) {
		this.id_locacao = id_locacao;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_inicial;
			
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_final;

	
	private String colaborador;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cidade getCidadeCliente() {
		return cidadeCliente;
	}

	public void setCidadeCliente(Cidade cidadeCliente) {
		this.cidadeCliente = cidadeCliente;
	}

	public Cidade getCidadeEvento() {
		return cidadeEvento;
	}


     public String Whats(){
          String telefoneFormatado = telefone.replace('(', ' ');
		  telefoneFormatado = telefoneFormatado.replace(')', ' ');
		  telefoneFormatado = telefoneFormatado.replace('-', ' ');
		return "https://wa.me/55"+ telefoneFormatado.replaceAll("\\s+","");
	 }

	public void setCidadeEvento(Cidade cidadeEvento) {
		this.cidadeEvento = cidadeEvento;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getOrigemContato() {
		return origemContato;
	}

	public void setOrigemContato(String origemContato) {
		this.origemContato = origemContato;
	}

	public String getEvolucao() {
		return evolucao;
	}

	public void setEvolucao(String evolucao) {
		this.evolucao = evolucao;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getRetorno() {
		return retorno;
	}

	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(String atendimento) {
		this.atendimento = atendimento;
	}

	public Date getData_inicial() {
		return data_inicial;
	}

	public void setData_inicial(Date data_inicial) {
		this.data_inicial = data_inicial;
	}

	public Date getData_final() {
		return data_final;
	}

	public void setData_final(Date data_final) {
		this.data_final = data_final;
	}


	public String getColaborador() {
		return colaborador;
	}

	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
