package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Pagamento;
@Repository
@Transactional
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	@Query(value="select c.* from pagamento c where UPPER(c.descricao) like %?1%", nativeQuery=true)
	List<Pagamento> findPagamentoByName(String nome);
	
	@Query(value="select c.* from pagamento c where where data between %1 and %2" , nativeQuery=true)
	List<Pagamento> findPagamentoDatas(String dataInicial, String DataFinal);
	
	@Query(value="select * from pagamento order by id desc limit 10", nativeQuery=true)
	List<Pagamento> top10();
}
