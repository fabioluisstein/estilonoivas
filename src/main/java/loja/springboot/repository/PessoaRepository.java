package loja.springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import loja.springboot.model.Pessoa;

@Transactional
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
	@Query("select p from Pessoa p where p.nome like %?1% ")
	List<Pessoa> findPessoaByName(String nome);
}
