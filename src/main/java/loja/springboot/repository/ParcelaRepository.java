package loja.springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Parcela;
@Repository
@Transactional
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
	@Query(value="select l.* from parcela l  where l.idlocacao = ?1 ", nativeQuery=true)
	List<Parcela> findLocacaoById(Long id);
	
	@Query(value="Select * from parcela p  where  p.data_pagamento  BETWEEN ?1 AND  ?2 ", nativeQuery=true)
	List<Parcela> findLocacaoDatas(String dataInicial, String DataFinal);
	 
	@Query(value="select * from parcela order by id desc limit 10", nativeQuery=true)
	List<Parcela> top10();
}
    