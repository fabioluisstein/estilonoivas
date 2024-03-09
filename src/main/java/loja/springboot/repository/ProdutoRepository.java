package loja.springboot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import loja.springboot.model.Produto;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query(value = "Select id, fornecedor, categoria, cor, tamanho, data_compra, valor,  foto   from vw_datatable_produtos order by id desc", nativeQuery = true)
	List<listProdutos> listaTodos();
	public static interface listProdutos {
		Long getId(); 
		String getFornecedor();
		String getCategoria();
		String getCor();
		String getTamanho();
		Date   getData_compra();
		Double getValor();
		String getFoto();
	}

	@Query(value = "Select id, fornecedor, categoria, cor, tamanho, data_compra, valor,  foto   from vw_datatable_produtos order by id desc", nativeQuery = true)
	List<listTodosProdutos> listProdutos();
	public static interface listTodosProdutos { 
		Long getId();  
		String getFornecedor(); 
		String getCategoria();
		String getCor();
		String getTamanho();
		Date   getData_compra();
		Double getValor();
		String getFoto();
		
	} 
	@Query(value = " Select id, fornecedor, categoria, cor, tamanho, data_compra, valor,  foto   from vw_datatable_produtos  where id like %:search%  or  fornecedor like %:search% " +
	" or  categoria like %:search% or cor like %:search% or tamanho like %:search%  or data_compra like %:search%  or valor like %:search% " , nativeQuery = true)
    Page<listTodosProdutos> findByProduto(@Param("search") String search, Pageable pageable); 

	@Query(value = " Select id, fornecedor, categoria, cor, tamanho, data_compra, valor,  foto   from vw_datatable_produtos order by id desc ", nativeQuery = true)
	Page<listTodosProdutos> findByProdutoPage(@Param("id") Long id, Pageable pageable);


}
