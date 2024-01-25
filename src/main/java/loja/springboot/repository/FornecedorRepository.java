package loja.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Fornecedor;

@Transactional
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query(value = "select id, nome, telefone, cidade from vw_datatable_fornecedores  order by nome asc ", nativeQuery = true)
	List<listFornecedores> fornecedoresTodos();
	public static interface listFornecedores {
		Long getId(); 
		String getNome();
		String getTelefone();
		String getCidade();
	}


	@Query(value = "select id, nome, telefone, cidade from vw_datatable_fornecedores a  where a.nome like %:search%  or  a.id like %:search% or telefone  like %:search% or  cidade  like %:search% ", nativeQuery = true)

	Page<listFornecedores> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageable);

	@Query(value = "select id, nome, telefone, cidade from vw_datatable_fornecedores   order by id desc ", nativeQuery = true)
	Page<listFornecedores> findBySite(@Param("id") Long id, Pageable pageable);




}
