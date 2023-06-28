package loja.springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import loja.springboot.dto.GraficoDTO;

public interface GraficoRepository extends JpaRepository<GraficoDTO, Long> {
	// @Query(value="select new loja.springboot.dto.GraficoDTO(mes,
	// despesas,entradas) from vw_receita_despesas", nativeQuery=true)
	// List<GraficoDTO> despesasReceitas();
 	
	@Query(value = "Select 'Entrada' as tipo, meses as mes ,  entradas as valor   from vw_receita_despesas UNION ALL " + 
	" Select 'Saida' as tipo, meses as mes ,  despesas as valor   from vw_receita_despesas UNION ALL  " +  
	" Select 'Liquido' as tipo, meses as mes ,  (entradas-despesas) as valor   from vw_receita_despesas", nativeQuery = true)
	List<listGraficoMes> graficoMes();
        public static interface listGraficoMes {
			String getTipo(); 
			String getMes(); 
			Double getValor(); 
        } 

		@Query(value = "Select papel, valor from painel_papel_clientes", nativeQuery = true)
		List<listGraficoPapelCliente> graficoPapelCliente();
			public static interface listGraficoPapelCliente {
				String getPapel(); 
				Double getValor(); 
			} 

		
		@Query(value = " Select empresa,liquido,cliente,ticket,valuation from painel_indicador_principal", nativeQuery = true)
		List<listGraficoPrincipal> graficoPrincipal();
			public static interface listGraficoPrincipal {
				String getEmpresa(); 
				String getLiquido(); 
				String getCliente(); 
				String getTicket(); 
				String getValuation(); 
			} 

		
			@Query(value = " Select locacaoFutura, locadoHoje, tempo, prova  from painel_indicador_card", nativeQuery = true)
			List<listGraficoCard> graficoCard();
				public static interface listGraficoCard {
					String getLocacaoFutura(); 
					String getLocadoHoje(); 
					String getTempo(); 
					String getProva(); 
				}  

			
			@Query(value = " Select origem,  valor, tabela from painel_origem_clientes", nativeQuery = true)
			List<listGraficoOrigemCLiente> graficoOrigemCliente();
				public static interface listGraficoOrigemCLiente {
					String getOrigem(); 
					Double getValor(); 
					String getTabela(); 
					
				} 

	
				@Query(value = " Select Locacao, Oportunidades, Indice,  EventoFuturos from painel_indicador_secundario", nativeQuery = true)
				List<listGraficoSecundario> graficoSecundario();
					public static interface listGraficoSecundario {
						String getLocacao(); 
						String getOportunidades(); 
						String getIndice(); 
						String getEventoFuturos(); 
					} 
	


				

}
 