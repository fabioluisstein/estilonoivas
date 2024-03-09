package loja.springboot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import loja.springboot.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
 
}
 