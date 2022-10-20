package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Estado;
@Repository
@Transactional
public interface EstadoRepository extends JpaRepository<Estado, Long> {
	@Query("select p from Estado p where UPPER(p.nome) like %?1% OR UPPER(p.sigla) like %?1%")
	List<Estado> findEstadoByName(String nome);
	
	@Query(value="select * from estado order by id desc limit 10", nativeQuery=true)
	List<Estado> top10();
}
