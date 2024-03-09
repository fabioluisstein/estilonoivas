package loja.springboot.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import loja.springboot.service.UsuarioService;


@Configuration
@EnableWebSecurity
public class WebConfigSecurity{
    
    @Autowired
	private UsuarioService service;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.csrf()
		.disable() // Desativa as configurações padrão de memória.
		.authorizeHttpRequests() // Pertimir restringir acessos
        .requestMatchers("/materialize/**","/js/**","/css/**", "/images/**", "/consultaprodutos/**","/pesquisaprodutocustom/**").permitAll()
         
        // acessos privados admin
         .requestMatchers("/listacontasBancarias/**","/u/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()
        .and().formLogin().loginPage("/login").failureUrl("/login-error").permitAll();
        return http.build();
	}

 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
        
    }

}
    
		
	
