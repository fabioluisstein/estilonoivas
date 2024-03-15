package loja.springboot.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import loja.springboot.service.UsuarioService;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebConfigSecurity{
    
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception { 

        http.authorizeHttpRequests((authorize) -> authorize				
			// acessos públicos liberados
	
        .requestMatchers("/materialize/**","/js/**","/css/**", "/images/**", "/consultaprodutos/**","/pesquisaprodutocustom/**","/expired").permitAll()
         
        // acessos privados admin
         .requestMatchers("/listacontasBancarias/**","/u/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()

        )
    	.formLogin()
			.loginPage("/login")
			.failureUrl("/login-error")
			.permitAll()
		.and()
			.logout()
			.logoutSuccessUrl("/")
			.deleteCookies("JSESSIONID")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado")
		.and()
			.rememberMe();
        return http.build();
	}

 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("removal")
    @Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, 
													   PasswordEncoder passwordEncoder, 
													   UsuarioService userDetailsService) throws Exception {
		
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
				.and()
				.build();
	}
}
    
		
	
