package loja.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import loja.springboot.model.LoginInfo;

@Repository
public interface LoginRepository extends JpaRepository<LoginInfo, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário



    
}
