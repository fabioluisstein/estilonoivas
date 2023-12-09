package loja.springboot.repository;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cidade;

@Transactional
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	InterfaceGeneric interfaceGeneric = new InterfaceGeneric();

	@Cacheable("cidadesTodas") 
	@Query(value = "select  id,   nome,  estado,  estado_id , quantidadeCliente   from vw_datatable_cidades  order by nome asc ", nativeQuery = true)
	List<listCidades> listCidadades();
	public static interface listCidades {
		String getId(); 
		String getNome();
		String getEstado();
		Long getEstado_id();
		Long getQuantidadeCliente();
	}

	@Cacheable("cidadeDtoRelac") 
	@Query(value = "select id, nome  from vw_datatable_cidades order by  nome asc", nativeQuery = true)
	 List<InterfaceGeneric.listGeneric> cidadeDtoRelac(); 


	@Query(value = "select  id,   nome,  estado,  estado_id , quantidadeCliente   from vw_datatable_cidades  order by nome asc", nativeQuery = true)
	List<listTodasCidades> TodasCidades();
	public static interface listTodasCidades { 
	String getId(); 
		String getNome();
		String getEstado();
		Long getEstado_id();
		Long getQuantidadeCliente();
	}

	@Query(value = " select  id,   nome,  estado,  estado_id , quantidadeCliente   from vw_datatable_cidades  where id like %:search%  or  nome like %:search% " +
	" or  estado like %:search%  or quantidadeCliente like %:search%  " , nativeQuery = true)
    Page<listTodasCidades> findByCidade(@Param("search") String search, Pageable pageable);

	@Query(value = " select  id,   nome,  estado,  estado_id , quantidadeCliente   from vw_datatable_cidades  order by nome asc ", nativeQuery = true)
	Page<listTodasCidades> findByCidadesPage(@Param("id") Long id, Pageable pageable);
	


}