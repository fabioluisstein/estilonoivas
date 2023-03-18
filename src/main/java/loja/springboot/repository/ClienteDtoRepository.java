package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import loja.springboot.dto.dtoCliente;

public interface ClienteDtoRepository extends JpaRepository<dtoCliente, Long> {
 

	@Query( "select a from dtoCliente  a  where a.cidade_id= ?1")
	List<dtoCliente> listaClienteCidade(Long id);

}
 