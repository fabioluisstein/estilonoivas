package loja.springboot.repository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.LocacaoProduto;

@Transactional
public interface LocacaoProdutoRepository extends JpaRepository<LocacaoProduto, Long> {
	@Query(value = "select l.* from locacao_produto l  where l.idlocacao = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findLocacaoById(Long id);

	@Query(value = "Select * from locacao_produto p  where  p.data_liberacao  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<LocacaoProduto> findLocacaoDatas(String dataInicial, String DataFinal);

	@Query(value = "Select  id as id,  idLocacao as locacao, idProduto as produto, tipoProduto as tipo, cor, tamanho, cliente, status, dataLimite as liberacao, ajuste, foto, cidade, telefone, atendente, dias, outros FROM vw_datatable_ajustes ", nativeQuery = true)
	List<listLocacaoProduto> locacoesProdutos();
	public static interface listLocacaoProduto {
		Long getId(); 
		Long getLocacao(); 
		Long getProduto(); 
		String getTipo();
		String getCor();
		String getTamanho();
		String getCliente();
		String getStatus();
		Date getLiberacao();
		String getAjuste();
		String getFoto();
		String getCidade();
		String getTelefone();
		String getAtendente();
		String getDias();
		String getOutros();
		
	}

	@Query(value = "Select id as id, idLocacao as locacao, idProduto as produto, tipoProduto as tipo, cor, tamanho, cliente, status, dataLimite as liberacao, ajuste, foto, cidade, telefone, atendente, dias, outros  " +
	" from vw_datatable_ajustes a  where a.idLocacao like %:search%  or  a.idProduto like %:search% or  a.tipoProduto like %:search%  or  a.cor like %:search% " +
	" or  a.tamanho like %:search%     or  a.cliente like %:search%   or  a.status like %:search%  or  a.dataLimite like %:search% " , nativeQuery = true)
	Page<listLocacaoProduto> findByAjuste(@Param("search") String search, Pageable pageable);

	@Query(value = "select l.* from locacao_produto l  where l.produto_id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoById(Long id);

	@Query(value = "Select id as id, idLocacao as locacao, idProduto as produto, tipoProduto as tipo, cor, tamanho, cliente, status, dataLimite as liberacao, ajuste, foto, cidade, telefone, atendente, dias, outros  from vw_datatable_ajustes  where id = ?1 ", nativeQuery = true)
	listLocacaoProduto findAjusteById(Long id); 

	@Query(value = "select l.* from locacao_produto l  where l.id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocaoById(Long id); 

	@Query(value = "select l.* from locacao_produto l, locacao lc  where l.produto_id = ?1  and lc.id = l.idlocacao  order by lc.data_retirada desc", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocacacoesById(Long id);


}
