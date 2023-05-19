package loja.springboot.repository;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Produto;
import loja.springboot.model.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

	@Query("select p from Promocao p where p.preco = :preco")
	Page<Promocao> findByPreco(@Param("preco") BigDecimal preco, Pageable pageable);
	
	@Query("select p from Promocao p where p.titulo like %:search% "
			+ "or p.site like %:search% "
			+ "or p.categoria.titulo like %:search%")
	Page<Promocao> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageable);

	@Transactional(readOnly = false)
	@Modifying
	@Query("update Produto p set p.likes = p.likes + 1 where p.id = :id")
	void updateSomarLikes(@Param("id") Long id);
	
	@Query("select p.likes from Produto p where p.id = :id")
	int findLikesById(@Param("id") Long id);

    @Query("Select  p from Produto p where p.id   = :id")
	Page<Produto> findBySite(@Param("id") Long id, Pageable pageable);
 
    @Query(value = "Select id,  campoPesquisa from vw_pesquisa_produto where campoPesquisa like  %:site% ", nativeQuery = true)
	List<String> findSitesByTermo(@Param("site") String site);

 

}
