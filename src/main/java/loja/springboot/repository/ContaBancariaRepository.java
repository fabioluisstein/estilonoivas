package loja.springboot.repository;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.ContaBancaria;

@Transactional
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

	@Cacheable("contasBancariasTodas") 
	@Query(value = "select * from conta_bancaria order by id desc", nativeQuery = true)
	List<ContaBancaria> listContasBancarias();
	
	
	@Query(value = "select id, instituicao, tipo, valor , data from conta_bancaria order by id desc", nativeQuery = true)
	List<listTodasContas> TodasContasTodos();
	public static interface listTodasContas { 
		Long getId(); 
		String getInstituicao();
		String getTipo();
		Double getValor();
		Date getData();
		
	}
	@Query(value = " Select id, instituicao, tipo, valor, data from conta_bancaria a  where a.id like %:search%  " , nativeQuery = true)
    Page<listTodasContas> findByConta(@Param("search") String search, Pageable pageable);

	@Query(value = "select id, instituicao, tipo, valor, data from conta_bancaria order by id desc ", nativeQuery = true)
	Page<listTodasContas> findByContPage(@Param("id") Long id, Pageable pageable);
	

}
