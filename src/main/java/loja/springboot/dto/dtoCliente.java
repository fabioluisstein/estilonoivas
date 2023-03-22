package loja.springboot.dto;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Cacheable
@Entity
@Immutable
@Subselect("select id, nome, telefone, whats, cpf, cidade, cidade_id from vw_datatable_clientes")

public class dtoCliente implements Serializable {
 
	@Id 
	private Long id;
	private String nome;
	private String telefone;
	private String whats;
	private String cpf;
	private String cidade; 
	private Long cidade_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCidade_id() {
		return cidade_id;
	}

	public void setCidade_id(Long cidade_id) {
		this.cidade_id = cidade_id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getWhats() {
		return whats;
	}

	public void setWhats(String whats) {
		this.whats = whats;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}



}

