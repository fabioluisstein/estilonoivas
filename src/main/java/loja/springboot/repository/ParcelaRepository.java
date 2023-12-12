package loja.springboot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Parcela;

@Transactional
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
	
	@Query(value = "select l.* from parcela l  where l.idlocacao = ?1 ", nativeQuery = true)
	List<Parcela> findLocacaoById(Long id);
/* 
	@Query(value = "Select parcela.id, parcela.data_pagamento, parcela.data_vencimento, parcela.moeda, parcela.observacao, parcela.numero_nf as NumeroNF,  parcela.valor, parcela.idlocacao, cliente.nome as cliente,  cliente.cpf as cpf,   cidade.nome as cidade,  parcela.nome_arquivo  as arquivo FROM  parcela, locacao, cliente, cidade where locacao.id = parcela.idlocacao AND " +
	"   cliente.cidade_id = cidade.id  and  locacao.cliente_id = cliente.id AND  parcela.data_pagamento  BETWEEN ?1 AND  ?2 order by  parcela.data_pagamento  desc", nativeQuery = true)
	List<listParcelaDTO> findLocacaoDatas(String dataInicial, String DataFinal);
*/
	@Query(value = " Select id, cliente,cpf ,  cidade, NumeroNF, observacao,  pagamento, vencimento, moeda,   valor,   atendente,  arquivo FROM  vw_datatable_parcelas", nativeQuery = true)
	List<listParcelasNf> parcelaMesAtual();
      public static interface listParcelasNf {
         Long getId(); 
		 String getCliente();
		 String getCpf();  
		 String getCidade(); 
		 String getNf(); 
		 String getObservacao();
	     Date   getPagamento();
		 Date   getVencimento();
		 String getMoeda(); 
		 Double getValor(); 
		 String getAtendente(); 
		 String getArquivo(); 
        }  

	@Query(value = " Select id, cliente,cpf ,  cidade, nf, observacao, pagamento, vencimento,  moeda,   valor,   atendente,  arquivo FROM  vw_datatable_parcelas  where id like %:search%  or  pagamento like %:search% " +
	" or  vencimento like %:search% or moeda like %:search% or observacao like %:search%  or nf like %:search%  or valor like %:search% or idlocacao like %:search% or cliente like %:search%  or cpf like %:search%  or cidade like %:search%  or atendente like %:search%  " , nativeQuery = true)
    Page<listParcelasNf> findByParcela(@Param("search") String search, Pageable pageable); 

    @Query(value = " Select id, cliente,cpf ,  cidade, nf, observacao, pagamento, vencimento,  moeda,   valor,   atendente,  arquivo FROM  vw_datatable_parcelas ", nativeQuery = true)
	Page<listParcelasNf> findByParcelaPage(@Param("id") Long id, Pageable pageable);





}
