package loja.springboot.model;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Cacheable
public class Pessoa implements Serializable {
 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nome;
	private String cpf;
	private String email;
	
	@OneToMany(mappedBy = "colaborador")
    private List<Locacao> locacoes;
		
	public List<Locacao> getLocacoes() {
		return locacoes;
	}
	public void setLocacoes(List<Locacao> locacoes) {
		this.locacoes = locacoes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
	
}
