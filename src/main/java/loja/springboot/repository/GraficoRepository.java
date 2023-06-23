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


}
 