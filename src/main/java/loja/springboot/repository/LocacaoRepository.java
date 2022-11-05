package loja.springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Locacao;
@Repository
@Transactional
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
	@Query(value="select l.* from locacao l, cliente c where l.cliente_id = c.id and "
			+ " (  UPPER(c.nome)  like %?1% or l.id like %?1% )", nativeQuery=true)
	List<Locacao> findLocacaoByName(String nome);
	
	@Query(value="Select * from locacao p  where  p.data_locacao  BETWEEN ?1 AND  ?2 ", nativeQuery=true)
	List<Locacao> findLocacaoDatas(String dataInicial, String DataFinal);
	 
	@Query(value="select * from locacao order by id desc limit 10", nativeQuery=true)
	List<Locacao> top10();
	

	
}
  