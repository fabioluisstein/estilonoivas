package loja.springboot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import loja.springboot.model.Categorias;

public interface CategoriasRepository extends JpaRepository<Categorias, Long> {

}
