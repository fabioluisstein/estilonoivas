package loja.springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.LocacaoProduto;

@Transactional
public interface LocacaoProdutoRepository extends JpaRepository<LocacaoProduto, Long> {
	@Query(value = "select l.* from locacao_produto l  where l.idlocacao = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findLocacaoById(Long id);

	@Query(value = "Select * from locacao_produto p  where  p.data_liberacao  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<LocacaoProduto> findLocacaoDatas(String dataInicial, String DataFinal);

	@Query(value = "select locacao_produto.* from locacao_produto, locacao where  locacao_produto.idlocacao = locacao.id AND  locacao.data_retirada BETWEEN ADDDATE(now(), INTERVAL -5 DAY) and ADDDATE(now(), INTERVAL +25 DAY) order by  locacao.data_retirada asc", nativeQuery = true)
	List<LocacaoProduto> locacoesProdutos();

	@Query(value = "select l.* from locacao_produto l  where l.produto_id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoById(Long id);

	@Query(value = "select l.* from locacao_produto l  where l.id = ?1 ", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocaoById(Long id); 

	@Query(value = "select l.* from locacao_produto l, locacao lc  where l.produto_id = ?1  and lc.id = l.idlocacao and lc.data_retirada > ADDDATE(now(), INTERVAL -5 DAY) ", nativeQuery = true)
	List<LocacaoProduto> findProdutoLocacacoesById(Long id);


}
