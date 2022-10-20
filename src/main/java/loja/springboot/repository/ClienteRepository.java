package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Cliente;
@Repository
@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	@Query(value="select c.* from cliente c where UPPER(c.nome) like %?1% OR  UPPER(c.cpf) like %?1% OR UPPER(c.telefone) like %?1%", nativeQuery=true)
	List<Cliente> findClienteByName(String nome);

	@Query(value="select * from cliente order by id desc limit 10", nativeQuery=true)
	List<Cliente> top10();
}
