package loja.springboot.repository;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cliente;

@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	@Query(value = "select c.* from cliente c where UPPER(c.nome) like %?1% OR  UPPER(c.cpf) like %?1% OR UPPER(c.telefone) like %?1%", nativeQuery = true)
	List<Cliente> findClienteByName(String nome);

	@Query(value = "select l.* from cliente l  where l.id = ?1 ", nativeQuery = true)
	List<Cliente> findLocacaoById(Long id);
	
	@Query( value = "select a from vw_datatable_clientes  a  where a.cidade_id= ?1", nativeQuery = true)
	List<listClientes> listaClienteCidade(Long id);

	@Cacheable("clienteTodosDto") 
	@Query(value = "select id, nome, telefone, whats, cpf,  cidade_id,  cidade from vw_datatable_clientes a  order by id desc ", nativeQuery = true)
	List<listClientes> clientesTodos();
	public static interface listClientes {
		Long getId(); 
		String getNome();
		String getTelefone();
		String getWhats();
		String getCpf();
		Long getCidade_id();
		String getCidade();
	}



}
