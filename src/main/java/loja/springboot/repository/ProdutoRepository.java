package loja.springboot.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Produto;

@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	@Query(value = "select distinct c.* from produto c, fornecedor f where c.fornecedor_id = f.id and  UPPER(f.nome) like %?1% OR 	c.id like %?1%", nativeQuery = true)
	List<Produto> findProdutoByName(String nome);

	@Cacheable("listProdutos") 
	@Query(value = "select * from produto order by id desc", nativeQuery = true)
	List<Produto> listaTodos();






}
