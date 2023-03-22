package loja.springboot.dto;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
@Subselect("Select * from vw_datatable_locacoes")
@Cacheable

public class dtoLocacao implements Serializable {
 
	@Id 
	private Long id;
	private Date data_locacao;
	private Date data_retirada;
	private String cliente;
	private String cidade;
	private String telefone;
	private String whats; 
	private Double total_produto;
	private Double falta_pagar;
	
	

	public Date getData_locacao() {
		return data_locacao;
	}
	public void setData_locacao(Date data_locacao) {
		this.data_locacao = data_locacao;
	}
	public Date getData_retirada() {
		return data_retirada;
	}
	public void setData_retirada(Date data_retirada) {
		this.data_retirada = data_retirada;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
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
	public Double getTotal_produto() {
		return total_produto;
	}
	public void setTotal_produto(Double total_produto) {
		this.total_produto = total_produto;
	}
	public Double getFalta_pagar() {
		return falta_pagar;
	}
	public void setFalta_pagar(Double falta_pagar) {
		this.falta_pagar = falta_pagar;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
}

