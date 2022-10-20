package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Categoria;
@Repository
@Transactional
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	@Query(value="select c.* from categoria c where UPPER(c.descricao) like %?1%", nativeQuery=true)
	List<Categoria> findCategoriaByName(String nome);
	
	@Query(value="select * from categoria order by id desc limit 10", nativeQuery=true)
	List<Categoria> top10();
	
	
	@Query(value="select * from categoria where tabela  = ?1 order by id desc", nativeQuery=true)
	List<Categoria> findCategoriaByTable(String tabela);
	
}
  