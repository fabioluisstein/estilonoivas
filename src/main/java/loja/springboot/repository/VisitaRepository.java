package loja.springboot.repository;

import java.util.Date;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Visita;

@Transactional
public interface VisitaRepository extends JpaRepository<Visita, Long> {
	
	@Cacheable("listVisitas") 
	@Query(value = "SELECT visita.id, nome_cliente,  colaborador, tipo_cliente, data_inicial, status, data_final, evolucao, origem_contato, retorno, telefone, cidade.nome as cidade_cliente FROM visita left join cidade on  visita.cidade_cliente = cidade.id ", nativeQuery = true)
	List<listVisitas> listaTodos();
	public static interface listVisitas {
		Long getId(); 
		String getNome_cliente();
		String getColaborador();
		String getTipo_cliente();
		Date getData_inicial();
		String getStatus(); 
		Date getData_Final();
		String getEvolucao();
		String getOrigem_contato();
		String getRetorno();
		String getTelefone();
		String getCidade_cliente();
	}

}
