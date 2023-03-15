package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cliente;

@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	@Query(value = "select c.* from cliente c where UPPER(c.nome) like %?1% OR  UPPER(c.cpf) like %?1% OR UPPER(c.telefone) like %?1%", nativeQuery = true)
	List<Cliente> findClienteByName(String nome);

	@Query(value = "select * from cliente ", nativeQuery = true)
	List<Cliente> listaClientes();

	@Query(value = "select l.* from cliente l  where l.id = ?1 ", nativeQuery = true)
	List<Cliente> findLocacaoById(Long id);
	
	@Query(value = "select l.* from cliente l  where cidade_id= ?1", nativeQuery = true)
	List<Cliente> listaClienteCidade(Long id);

	


}
