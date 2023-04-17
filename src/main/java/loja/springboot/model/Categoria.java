package loja.springboot.model;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Cacheable
@Table(name="categoria")
public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String tabela;

	
	@OneToMany(mappedBy = "categoriaAjuste")
    private List<LocacaoProduto> locacaoProduto;

	@OneToMany(mappedBy = "categoria")
    private List<Produto> produto;

	@OneToMany(mappedBy = "categoria")
    private List<Pagamento> pagamento;	

	@OneToMany(mappedBy = "evento")
    private List<Locacao> evento;	

	public List<Locacao> getEvento() {
		return evento;
	}

	public void setEvento(List<Locacao> evento) {
		this.evento = evento;
	}

	public List<Produto> getProduto() {
		return produto;
	}
	
	public List<Pagamento> getPagamento() {
		return pagamento;
	}

	public void setPagamento(List<Pagamento> pagamento) {
		this.pagamento = pagamento;
	}

	public void setProduto(List<Produto> produto) {
		this.produto = produto;
	}

	public List<LocacaoProduto> getLocacaoProduto() {
		return locacaoProduto;
	}

	public void setLocacaoProduto(List<LocacaoProduto> locacaoProduto) {
		this.locacaoProduto = locacaoProduto;
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

	public String getTabela() {
		return tabela;
	}
 
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	@Override
	public String toString() {
		return nome;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
