package loja.springboot.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import loja.springboot.model.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

}
