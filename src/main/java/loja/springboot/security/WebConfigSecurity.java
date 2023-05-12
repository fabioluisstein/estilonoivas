package loja.springboot.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebConfigSecurity{

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.csrf()
		.disable() // Desativa as configurações padrão de memória.
		.authorizeHttpRequests() // Pertimir restringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
		.anyRequest().authenticated()
		.and().formLogin().permitAll();
        return http.build();
	
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("fibonatti").password("{noop}123").roles("ADMIN")
        .and()
        .withUser("estilo").password("{noop}estilo321").roles("ADMIN");
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> {
            web.ignoring().antMatchers("/materialize/**");
            web.ignoring().antMatchers("/consultaprodutos/**");
            web.ignoring().antMatchers("/pesquisaprodutocustom/**");


        };
		
		
	}

}