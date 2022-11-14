package loja.springboot.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Grafico implements Serializable {

	private static final long serialVersionUID = 1L;
   
	private Long id;
	List<String> meses = new ArrayList<String>();
	List<Double> pagamentos = new ArrayList<Double>();
	List<Double> entradas = new ArrayList<Double>();
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<String> getMeses() {
		return meses;
	}
	public void setMeses(List<String> meses) {
		this.meses = meses;
	}
	public List<Double> getPagamentos() {
		return pagamentos;
	}
	public void setPagamentos(List<Double> pagamentos) {
		this.pagamentos = pagamentos;
	}
	public List<Double> getEntradas() {
		return entradas;
	}
	public void setEntradas(List<Double> entradas) {
		this.entradas = entradas;
	}
	
	
	
}
