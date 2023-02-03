package loja.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Fornecedor;

@Transactional
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
	@Query(value = "select c.* from fornecedor c where  UPPER(c.nome) like %?1% OR UPPER(c.telefone) like %?1%", nativeQuery = true)
	List<Fornecedor> findFornecedorByName(String nome);

	@Query(value = "select * from fornecedor", nativeQuery = true)
	List<Fornecedor> listaForcenedores();

	@Query(value = "select * from fornecedor order by nome asc", nativeQuery = true)
	List<Fornecedor> forcedorOrderBy();
}
