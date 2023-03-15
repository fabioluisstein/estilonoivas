package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Locacao;

@Transactional
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
	@Query(value = "select l.* from locacao l, cliente c where l.cliente_id = c.id and "
			+ " (  UPPER(c.nome)  like %?1% or l.id like %?1% )", nativeQuery = true)
	List<Locacao> findLocacaoByName(String nome);

	@Query(value = "Select * from locacao p  where  p.data_locacao  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<Locacao> findLocacaoDatas(String dataInicial, String DataFinal);

	@Query(value = "select * from locacao order by id desc", nativeQuery = true)
	List<Locacao> topTodas();

 
	@Query(value = "Select  * from locacao  where locacao.data_retirada < now() and (select sum(parcela.valor) as valor_parcela from parcela where  parcela.idlocacao = locacao.id ) < (select sum(locacao_produto.valor) as valor_produto from locacao_produto where  locacao_produto.idlocacao = locacao.id ) ", nativeQuery = true)
	List<Locacao> locacoesVencidas();



}
