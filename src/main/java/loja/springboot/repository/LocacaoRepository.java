package loja.springboot.repository;
import java.util.Date;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Locacao;

@Transactional
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
 
	@Query(value = "Select  *  from vw_datatable_locacoes locacao  where locacao.data_retirada < now() and (select sum(parcela.valor) as valor_parcela from parcela where  parcela.idlocacao = locacao.id ) < (select sum(locacao_produto.valor) as valor_produto from locacao_produto where  locacao_produto.idlocacao = locacao.id ) ", nativeQuery = true)
	List<listLocacoes> locacoesVencidas();

	@Query(value = "Select * from vw_datatable_locacoes p  where  p.data_locacao  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<listLocacoes> findLocacaoDatas(String dataInicial, String DataFinal);

	@Query(value = "Select * from vw_datatable_locacoes p  order by p.data_locacao desc ", nativeQuery = true)
	List<listLocacoes> findAllTodos();

	@Cacheable("locacoes120") 
	@Query(value = "Select * from vw_datatable_locacoes p  order by p.data_locacao desc limit 160 ", nativeQuery = true)
	List<listLocacoes> top120Locacao();
	public static interface listLocacoes {
		Long getId(); 
		Date getData_locacao();
		Date getData_retirada();
		String getCliente();
		String getCidade();
		String getTelefone();
		String getWhats();
		Double getTotal_produto();
		Double getFalta_pagar();
	}


}
