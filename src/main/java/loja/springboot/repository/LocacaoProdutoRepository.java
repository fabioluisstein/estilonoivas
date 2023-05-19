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

	@Query(value = "Select  id as id,  idLocacao as locacao, idProduto as produto, tipoProduto as tipo, cor, tamanho, cliente, status, dataLimite as liberacao FROM vw_datatable_ajustes ", nativeQuery = true)
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
	}

	@Query(value = "Select id as id, idLocacao as locacao, idProduto as produto, tipoProduto as tipo, cor, tamanho, cliente, status, dataLimite as liberacao  from vw_datatable_ajustes a  where a.idLocacao like %:search% ", nativeQuery = true)
	Page<listLocacaoProduto> findByAjuste(@Param("search") String search, Pageable pageable);

	@Query(value = "select l.* from locacao_produto l  where l.produto_id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoById(Long id);

	@Query(value = "select l.* from locacao_produto l  where l.id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocaoById(Long id); 

	@Query(value = "select l.* from locacao_produto l, locacao lc  where l.produto_id = ?1  and lc.id = l.idlocacao and lc.data_retirada > ADDDATE(now(), INTERVAL -5 DAY) ", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocacacoesById(Long id);


}
