package loja.springboot.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import loja.springboot.dto.dtoLocacao;

public interface LocacaoDtoRepository extends JpaRepository<dtoLocacao, Long> {

	@Query(value = "Select  *  from vw_datatable_locacoes locacao  where locacao.data_retirada < now() and (select sum(parcela.valor) as valor_parcela from parcela where  parcela.idlocacao = locacao.id ) < (select sum(locacao_produto.valor) as valor_produto from locacao_produto where  locacao_produto.idlocacao = locacao.id ) ", nativeQuery = true)
	List<dtoLocacao> locacoesVencidas();


	@Query(value = "Select * from vw_datatable_locacoes p  where  p.data_locacao  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<dtoLocacao> findLocacaoDatas(String dataInicial, String DataFinal);

	@Cacheable("locacoes120") 
	@Query(value = "Select * from vw_datatable_locacoes p  order by p.id desc limit 120 ", nativeQuery = true)
	List<dtoLocacao> top120Locacao();

}
  