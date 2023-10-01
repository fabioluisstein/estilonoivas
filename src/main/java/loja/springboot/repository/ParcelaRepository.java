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

	@Query(value = "Select parcela.id, parcela.data_pagamento, parcela.data_vencimento, parcela.moeda, parcela.observacao, parcela.numero_nf as NumeroNF,  parcela.valor, parcela.idlocacao, cliente.nome as cliente,  cliente.cpf as cpf,   cidade.nome as cidade,  parcela.nome_arquivo  as arquivo FROM  parcela, locacao, cliente, cidade where locacao.id = parcela.idlocacao AND " +
	"   cliente.cidade_id = cidade.id  and  locacao.cliente_id = cliente.id AND  parcela.data_pagamento  BETWEEN ?1 AND  ?2 order by  parcela.data_pagamento  desc", nativeQuery = true)
	List<listParcelaDTO> findLocacaoDatas(String dataInicial, String DataFinal);

	@Cacheable("listParcelasMesAtual") 
	@Query(value = "Select parcela.id, parcela.data_pagamento, parcela.data_vencimento, parcela.moeda, parcela.observacao, parcela.numero_nf as NumeroNF,  parcela.valor, parcela.idlocacao, cliente.nome as cliente,  cliente.cpf as cpf,   cidade.nome as cidade,  parcela.nome_arquivo  as arquivo FROM  parcela, locacao, cliente, cidade where locacao.id = parcela.idlocacao AND " +
                               "   cliente.cidade_id = cidade.id  and  locacao.cliente_id = cliente.id AND ((parcela.data_pagamento>=DATE(NOW())-40) OR  (parcela.data_vencimento>=DATE(NOW())-40)) order by  parcela.data_pagamento  desc", nativeQuery = true)
	List<listParcelaDTO> parcelaMesAtual();
        public static interface listParcelaDTO {
             Long getId(); 
			 Date getData_pagamento();
			 Date getData_vencimento();
			 String getMoeda(); 
			 String getNumeroNf(); 
			 String getObservacao(); 
			 Double getValor(); 
			 Long getIdlocacao(); 
			 String getCliente();  
			 String getCpf();  
			 String getCidade(); 
			 String getArquivo(); 
        }  



		
		@Query(value = "Select parcela.id, parcela.data_pagamento, parcela.data_vencimento,  parcela.moeda,  parcela.observacao,  parcela.numero_nf as NumeroNF,  parcela.valor,  parcela.idlocacao,  cliente.nome as cliente,  cliente.cpf as cpf,    cidade.nome as cidade,  parcela.nome_arquivo as arquivo  " +
 " FROM  parcela, locacao,  cliente, cidade  where locacao.id = parcela.idlocacao AND  cliente.cidade_id = cidade.id   AND  locacao.cliente_id = cliente.id AND  (YEAR(parcela.data_pagamento) = YEAR(NOW()) and  MONTH(parcela.data_pagamento) = MONTH(NOW()) ) and (parcela.arquivo is null or parcela.numero_nf is null)  order by  parcela.data_pagamento  desc", nativeQuery = true)
		List<listParcelaDTO> parcelaMesProblemas();
			



}
