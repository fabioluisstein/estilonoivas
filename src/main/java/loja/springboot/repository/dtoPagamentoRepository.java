package loja.springboot.repository;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import loja.springboot.dto.dtoPagamento;

public interface dtoPagamentoRepository extends JpaRepository<dtoPagamento, Long> {



    @Cacheable("pagamentos")
    @Query(value = "Select id , tipo ,data , data_param,  fornecedor, moeda ,origem ,valor , anexo from vw_pagamentos  order by id desc limit 120", nativeQuery = true)
	List<dtoPagamento> findAllPagamentos();
 
    @Cacheable("pagamentosTodos")
    @Query(value = "Select id , tipo ,data , data_param,  fornecedor, moeda ,origem ,valor , anexo from vw_pagamentos  order by id desc", nativeQuery = true)
	List<dtoPagamento> findAllPagamentosTodos();

}
 