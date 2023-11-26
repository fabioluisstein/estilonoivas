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
        .withUser("adm").password("{noop}admin1050").roles("ADMIN")
        .and()
        .withUser("fibonatti").password("{noop}fb1050").roles("ADMIN")
        .and()
        .withUser("danc").password("{noop}daneleti").roles("ADMIN")
        .and()
        .withUser("estilo").password("{noop}Decantador@01").roles("ADMIN");
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> {
            web.ignoring().antMatchers("/novo/**");
            web.ignoring().antMatchers("/materialize/**");
            web.ignoring().antMatchers("/consultaprodutos/**");
            web.ignoring().antMatchers("/pesquisaprodutocustom/**");
            web.ignoring().antMatchers("/consultarProdutoQr/**");
        };
		
		
	}

    

}