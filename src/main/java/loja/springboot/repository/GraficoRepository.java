package loja.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import loja.springboot.dto.GraficoDTO;

public interface GraficoRepository extends JpaRepository<GraficoDTO, Long> {
	// @Query(value="select new loja.springboot.dto.GraficoDTO(mes,
	// despesas,entradas) from vw_receita_despesas", nativeQuery=true)
	// List<GraficoDTO> despesasReceitas();

}
