package loja.springboot.security;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@Configuration
@EnableWebSecurity
public class WebConfigSecurity{

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.csrf()
		.disable() // Desativa as configurações padrão de memória.
		.authorizeHttpRequests() // Pertimir restringir acessos
        .antMatchers("/materialize/**","/js/**","/consultaprodutos/**","/pesquisaprodutocustom/**").permitAll()
		.anyRequest().authenticated()
        .and().formLogin().loginPage("/login").failureUrl("/login").permitAll();
        return http.build();
	}




    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("adm").password("{noop}admin1050").roles("ADMIN")
        .and()
        .withUser("fibonatti").password("{noop}fb1050").roles("ADMIN")
        .and()
        .withUser("danc").password("{noop}daneleti").roles("ADMIN")
        .and()
        .withUser("estilo").password("{noop}Decantador@01").roles("User");
    }
    
		
	

    

}