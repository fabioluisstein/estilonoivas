package loja.springboot.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cliente;

@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	@Query( value = "select * from vw_datatable_clientes  a  where a.cidade_id= ?1", nativeQuery = true)
	List<listTodosClientes> listaClienteCidade(Long id);


	@Query(value = "select id, nome, telefone, whats, cpf,  cidade_id,  cidade from vw_datatable_clientes a  order by id desc ", nativeQuery = true)
	List<listTodosClientes> clientesTodos();
	public static interface listTodosClientes { 
		Long getId(); 
		String getNome();
		String getTelefone();
		String getWhats();
		String getCpf();
		Long getCidade_id();
		String getCidade();
	}

	@Query( value = "select * from vw_datatable_clientes  a  where a.id= ?1", nativeQuery = true)
	listTodosClientes findClienteID(Long id);

	@Query(value = "select id, nome, telefone, whats, cpf,  cidade_id,  cidade from vw_datatable_clientes a  where a.nome like %:search%  or  a.id like %:search% or cpf  like %:search% or  cidade  like %:search%  or telefone  like %:search%", nativeQuery = true)

	Page<listTodosClientes> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageable);

	@Query(value = "select id, nome, telefone, whats, cpf,  cidade_id,  cidade from vw_datatable_clientes a  order by id desc ", nativeQuery = true)
	Page<listTodosClientes> findBySite(@Param("id") Long id, Pageable pageable);


  

}
