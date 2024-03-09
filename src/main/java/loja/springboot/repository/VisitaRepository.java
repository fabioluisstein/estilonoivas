package loja.springboot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import loja.springboot.model.Visita;


public interface VisitaRepository extends JpaRepository<Visita, Long> {
	
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



	@Query(value = "SELECT id, nome_cliente as cliente,  colaborador as atendente, tipo_cliente as tpcliente,  status, origem_contato as origem, data_final as datalimite FROM visita order by visita.id desc ", nativeQuery = true)
	List<listaVisitas> TodosVisitas();
	public static interface listaVisitas { 
		Long getId(); 
		String getCliente();
		String getAtendente();
		String getTpcliente();
		String getStatus();
		String getOrigem();
		String getDatalimite();
	}

	@Query(value = " SELECT id, nome_cliente as cliente,  colaborador as atendente, tipo_cliente as tpcliente, status, origem_contato as origem, data_final as datalimite FROM visita   where id like %:search%  or  nome_cliente like %:search% or  colaborador like %:search% or  tipo_cliente like %:search%" +
	" or  status like %:search%  or  origem_contato like %:search%  or  data_final like %:search% " , nativeQuery = true)
    Page<listaVisitas> findByVisitas(@Param("search") String search, Pageable pageable);

	@Query(value = "SELECT id, nome_cliente as cliente,  colaborador as atendente, tipo_cliente as tpcliente,  status, origem_contato as origem, data_final as datalimite FROM visita order by visita.id desc ", nativeQuery = true)
	Page<listaVisitas> findByVisitaPage(@Param("id") Long id, Pageable pageable);
	







}
