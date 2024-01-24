package loja.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Estado;

@Transactional
public interface EstadoRepository extends JpaRepository<Estado, Long> {
	InterfaceGeneric interfaceGeneric = new InterfaceGeneric();
		
	@Query(value = "select id, nome, sigla as uf from estado order by nome asc", nativeQuery = true)
	List<listTodosEstados> TodosEstados();
	public static interface listTodosEstados { 
		Long getId(); 
		String getNome();
		String getUf();
	}

	@Query(value = " select id, nome, sigla as uf from estado  where id like %:search%  or  nome like %:search% " +
	" or  sigla like %:search% " , nativeQuery = true)
    Page<listTodosEstados> findByEstado(@Param("search") String search, Pageable pageable);

	@Query(value = " select id, nome, sigla as uf from estado order by nome asc ", nativeQuery = true)
	Page<listTodosEstados> findByEstadosPage(@Param("id") Long id, Pageable pageable);
}

