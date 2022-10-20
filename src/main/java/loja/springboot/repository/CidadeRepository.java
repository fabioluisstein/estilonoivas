package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cidade;
@Repository
@Transactional
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	@Query(value="select c.* from cidade c where UPPER(c.nome) like %?1%", nativeQuery=true)
	List<Cidade> findCidadeByName(String nome);
	
	@Query(value="select * from cidade order by id desc limit 10", nativeQuery=true)
	List<Cidade> top10();
}
