package loja.springboot.model;
import java.io.Serializable;


public class GraficoAtendimento implements Serializable {

	private static final long serialVersionUID = 1L;
   
	private String qtdLocacao;
	private String ticket;
	private String indicadorGeral;
	private String locadoHoje;
	
	public String getQtdLocacao() {
		return qtdLocacao;
	}
	public void setQtdLocacao(String qtdLocacao) {
		this.qtdLocacao = qtdLocacao;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getIndicadorGeral() {
		return indicadorGeral;
	}
	public void setIndicadorGeral(String indicadorGeral) {
		this.indicadorGeral = indicadorGeral;
	}
	public String getLocadoHoje() {
		return locadoHoje;
	}
	public void setLocadoHoje(String locadoHoje) {
		this.locadoHoje = locadoHoje;
	}
	
	
	
}
