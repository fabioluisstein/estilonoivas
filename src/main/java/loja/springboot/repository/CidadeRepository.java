package loja.springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Cidade;

@Transactional
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	@Query(value = "select c.* from cidade c where UPPER(c.nome) like %?1%", nativeQuery = true)
	List<Cidade> findCidadeByName(String nome);

	@Query(value = "select  id,   nome,  estado,  estado_id , quantidadeCliente as quantidade_cliente   from vw_dataTable_cidades  order by id desc ", nativeQuery = true)
	List<Cidade> top10();

	@Query(value = "select  *   from cidade  order by  nome asc ", nativeQuery = true)
	List<Cidade> cidadesOrdem();

}
