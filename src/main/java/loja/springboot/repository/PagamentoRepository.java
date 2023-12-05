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
import loja.springboot.model.Pagamento;

@Transactional
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	
    @Query(value = "select id, tipo, data, fornecedor, moeda,  origem,  valor, anexo from vw_pagamentos  where data  BETWEEN ?1 AND  ?2  ", nativeQuery = true)
	List<listSaidas> findPagamentoDatas(String dataInicial, String DataFinal);

    @Cacheable("pagamentosTodos")
    @Query(value = "select id, tipo, data, fornecedor, moeda,  origem,  valor, anexo from vw_pagamentos  order by id desc", nativeQuery = true)
	List<listSaidas> findAllPagamentosTodos();

	@Cacheable("saidas") 
	@Query(value = "select id, tipo, data, fornecedor, moeda,  origem,  valor, anexo from vw_pagamentos a  order by id desc limit 60 ", nativeQuery = true)
	List<listSaidas> saidasTodos();
	public static interface listSaidas {
		Long getId(); 
		String getTipo();
		Date getData();
		String getFornecedor();
		String getMoeda();
		String getOrigem();
		Double getValor();
		String getCidade();
		String getAnexo();
	}

	@Query(value = "select id, tipo, data, fornecedor, moeda,  origem,  valor from vw_pagamentos a  order by id ", nativeQuery = true)
	List<listaSaidasGerais> saidasGeral();
	public static interface listaSaidasGerais {
		Long getId(); 
		String getTipo();
		Date getData();
		String getFornecedor();
		String getMoeda();
		String getOrigem();
		Double getValor();
	}

	@Cacheable("saidasRestrito") 
	@Query(value = "select id, tipo, data, fornecedor, moeda,  origem,  valor, anexo from vw_pagamentos a  where restrito ='Nao' order by id desc limit 60 ", nativeQuery = true)
	List<listSaidas> saidasTodosGerais();

	@Query(value = " Select id, tipo, data, fornecedor, moeda,  origem,  valor  from vw_pagamentos  where id like %:search%  or  tipo like %:search% " +
	" or  data like %:search% or fornecedor like %:search% or moeda like %:search%  or origem like %:search%  or valor like %:search% " , nativeQuery = true)
    Page<listaSaidasGerais> findByPagamento(@Param("search") String search, Pageable pageable); 

	@Query(value = " Select id, tipo, data, fornecedor, moeda,  origem,  valor  from vw_pagamentos order by id desc ", nativeQuery = true)
	Page<listaSaidasGerais> findByPapamentoPage(@Param("id") Long id, Pageable pageable);



}
