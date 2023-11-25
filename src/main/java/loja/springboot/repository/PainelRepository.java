package loja.springboot.repository;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import loja.springboot.dto.GraficoDTO;

public interface PainelRepository extends JpaRepository<GraficoDTO, Long> {
 
	@Cacheable("painelOperacional")
	@Query(value = " SELECT ticket, locadoHoje locado, indice, locacoes FROM vw_painel_operacional_telas ", nativeQuery = true)
    List<listPainelOperacional> grafico(); 
        public static interface listPainelOperacional {
			String getTicket(); 
			String getLocado();  
			String getIndice(); 
			String getLocacoes();
        } 
}
 