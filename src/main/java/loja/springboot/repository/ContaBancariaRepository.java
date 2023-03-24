package loja.springboot.repository;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.ContaBancaria;

@Transactional
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
	
	@Cacheable("contasBancariasTodas") 
	@Query(value = "select * from conta_bancaria order by id desc", nativeQuery = true)
	List<ContaBancaria> listContasBancarias();
}
