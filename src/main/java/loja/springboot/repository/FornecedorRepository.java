package loja.springboot.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Fornecedor;

@Transactional
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
	@Query(value = "select c.* from fornecedor c where  UPPER(c.nome) like %?1% OR UPPER(c.telefone) like %?1%", nativeQuery = true)
	List<Fornecedor> findFornecedorByName(String nome);

	@Cacheable("forncedoresTodosDto") 
	@Query(value = "select id, nome, telefone, cidade from vw_datatable_fornecedores  order by nome asc ", nativeQuery = true)
	List<listFornecedores> fornecedoresTodos();
	public static interface listFornecedores {
		Long getId(); 
		String getNome();
		String getTelefone();
		String getCidade();
	}


	@Query(value = "select * from fornecedor order by nome asc", nativeQuery = true)
	List<Fornecedor> forcedorOrderBy();

}
