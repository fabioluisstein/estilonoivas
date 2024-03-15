package loja.springboot.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;
    private String usuario;
    private String operacao;
    
    @CreationTimestamp	
	@Column(name = "horaLogin", nullable = true)
	private LocalDateTime horaLogin;

    public LoginInfo() {
        // Construtor vazio necessário para o JPA
    }

    public LoginInfo(String ip, String usuario,  String operacao) {
        this.ip = ip;
        this.usuario = usuario;
        this.operacao = operacao;
       
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getIp() {
        return ip;
    }





    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getHoraLogin() {
        return horaLogin;
    }

    public void setHoraLogin(LocalDateTime horaLogin) {
        this.horaLogin = horaLogin;
    }
}
