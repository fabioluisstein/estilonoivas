package loja.springboot.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import loja.springboot.dto.dtoPagamento;

public interface dtoPagamentoRepository extends JpaRepository<dtoPagamento, Long> {

    @Query(value = "Select id , tipo ,data , data_param,  fornecedor, moeda ,origem ,valor , anexo from vw_pagamentos  where data  BETWEEN ?1 AND  ?2  ", nativeQuery = true)
	List<dtoPagamento> findPagamentoDatas(String dataInicial, String DataFinal);

    @Query(value = "Select id , tipo ,data , data_param,  fornecedor, moeda ,origem ,valor , anexo from vw_pagamentos ", nativeQuery = true)
	List<dtoPagamento> findAllPagamentos();
 
}
 