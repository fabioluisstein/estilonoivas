package loja.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import loja.springboot.dto.GraficoDTO;
@Repository


public interface GraficoRepository extends JpaRepository<GraficoDTO, Long> {
	// @Query(value="select new loja.springboot.dto.GraficoDTO(mes, despesas,entradas) from vw_receita_despesas", nativeQuery=true)
	 //List<GraficoDTO> despesasReceitas();

}
