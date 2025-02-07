package loja.springboot.repository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import loja.springboot.model.ContaBancaria;


public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

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
	
	@Query(value = " Select id, instituicao, tipo, valor, data from conta_bancaria   where id like %:search%  or  instituicao like %:search% " +
	" or  tipo like %:search% or valor like %:search% or data like %:search% " , nativeQuery = true)
    Page<listTodasContas> findByConta(@Param("search") String search, Pageable pageable);

	@Query(value = " Select id, instituicao, tipo, valor, data from conta_bancaria   where  id = 0 " , nativeQuery = true)
    Page<listTodasContas> findByContaRestrito(@Param("search") String search, Pageable pageable);

	@Query(value = "select id, instituicao, tipo, valor, data from conta_bancaria order by id desc ", nativeQuery = true)
	Page<listTodasContas> findByContPage(@Param("id") Long id, Pageable pageable);
	

}
