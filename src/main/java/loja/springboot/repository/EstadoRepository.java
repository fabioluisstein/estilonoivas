package loja.springboot.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import loja.springboot.model.Estado;

@Transactional
public interface EstadoRepository extends JpaRepository<Estado, Long> {
	InterfaceGeneric interfaceGeneric = new InterfaceGeneric();
	
	@Cacheable("listEstados") 
	@Query(value = "select * from estado order by nome  asc ", nativeQuery = true)
	List<Estado> listEstados();

	@Cacheable("estadoDtoFilter") 
	@Query(value = "select id, nome from estado where lower(nome) like %?1%", nativeQuery = true)
	List<InterfaceGeneric.listGeneric> filtradas(String nome);
	

}

