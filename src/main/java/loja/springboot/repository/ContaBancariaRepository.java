package loja.springboot.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.ContaBancaria;

@Transactional
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
	@Query(value = "select c.* from conta_bancaria c where UPPER(c.instituicao) like %?1%", nativeQuery = true)
	List<ContaBancaria> findContaByName(String nome);

	@Query(value = "select * from conta_bancaria order by id desc", nativeQuery = true)
	List<ContaBancaria> top10();
}
