package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Pagamento;

@Transactional
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	@Query(value = "select c.* from pagamento c where UPPER(c.descricao) like %?1%", nativeQuery = true)
	List<Pagamento> findPagamentoByName(String nome);

	@Query(value = "Select * from pagamento p  where  p.data  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<Pagamento> findPagamentoDatas(String dataInicial, String DataFinal);

	@Query(value = "select * from pagamento", nativeQuery = true)
	List<Pagamento> listaPagamentos();

	@Query(value = "select * from pagamento where month(data) = month(NOW()) ", nativeQuery = true)
	List<Pagamento> pagamentoMesAtual();

}
