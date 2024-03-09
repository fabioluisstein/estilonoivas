package loja.springboot.model;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="estado")
public class Estado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String sigla;
		
	@OneToMany(mappedBy = "estado")
    private List<Cidade> clidades ;

	public Estado(){
	
	} 
	public Estado(Long id){
		this.id = id;
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

	public String getSigla() {
		return sigla;
	}
 
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	
	public List<Cidade> getClidades() {
		return clidades;
	}
	public void setClidades(List<Cidade> clidades) {
		this.clidades = clidades;
	}
	@Override
	public String toString() {
		return nome;
	}


}
