package loja.springboot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Parcela;

@Transactional
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
	
	@Query(value = "select l.* from parcela l  where l.idlocacao = ?1 ", nativeQuery = true)
	List<Parcela> findLocacaoById(Long id);

	@Query(value = "Select * from parcela p  where  p.data_pagamento  BETWEEN ?1 AND  ?2 ", nativeQuery = true)
	List<Parcela> findLocacaoDatas(String dataInicial, String DataFinal);

	@Cacheable("listParcelasMesAtual") 
	@Query(value = "Select parcela.id, parcela.data_pagamento, parcela.data_vencimento, parcela.moeda, parcela.observacao, parcela.valor, parcela.idlocacao, cliente.nome as cliente, parcela.nome_arquivo  as arquivo FROM  parcela, locacao, cliente where locacao.id = parcela.idlocacao AND " +
                               " locacao.cliente_id = cliente.id AND ((parcela.data_pagamento>=DATE(NOW())-40) OR  (parcela.data_vencimento>=DATE(NOW())-40)) order by  parcela.data_pagamento  desc", nativeQuery = true)
	List<listParcelaDTO> parcelaMesAtual();
        public static interface listParcelaDTO {
             Long getId(); 
			 Date getData_pagamento();
			 Date getData_vencimento();
			 String getMoeda(); 
			 String getObservacao(); 
			 Double getValor(); 
			 Long getIdlocacao(); 
			 String getCliente(); 
			 String getArquivo(); 
        } 
}
