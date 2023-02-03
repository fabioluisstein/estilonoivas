package loja.springboot.loja;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;


@SpringBootApplication
@EntityScan(basePackages="loja.springboot.*")
@ComponentScan(basePackages= {"loja.*"})
@EnableJpaRepositories(basePackages = {"loja.springboot.repository"})
@EnableTransactionManagement
@EnableCaching

public class LojaApplication {
	public static void main(String[] args) {
		SpringApplication.run(LojaApplication.class, args);
	}
	   
	 @Bean 
	    public LocaleResolver localeResolver(){
	        return new FixedLocaleResolver(new Locale("pt", "BR"));
	    }
	  
}
      