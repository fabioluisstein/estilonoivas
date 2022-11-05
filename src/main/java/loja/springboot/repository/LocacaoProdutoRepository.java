package loja.springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.LocacaoProduto;
@Repository
@Transactional
public interface LocacaoProdutoRepository extends JpaRepository<LocacaoProduto, Long> {
	@Query(value="select l.* from locacao_produto l  where l.idlocacao = ?1 ", nativeQuery=true)
	List<LocacaoProduto> findLocacaoById(Long id);
	
	@Query(value="Select * from locacao_produto p  where  p.data_liberacao  BETWEEN ?1 AND  ?2 ", nativeQuery=true)
	List<LocacaoProduto> findLocacaoDatas(String dataInicial, String DataFinal);
	 
	@Query(value="select * from locacao_produto order by id desc limit 10", nativeQuery=true)
	List<LocacaoProduto> top10();
	
	
	@Query(value="select l.* from locacao_produto l  where l.produto_id = ?1 ", nativeQuery=true)
	List<LocacaoProduto> findProdutoById(Long id);
	
}
   