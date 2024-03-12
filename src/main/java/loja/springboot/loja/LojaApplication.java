package loja.springboot.loja;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@SpringBootApplication
@EntityScan(basePackages = "loja.springboot.*")
@ComponentScan(basePackages = { "loja.*" })
@EnableJpaRepositories(basePackages = { "loja.springboot.repository" })
@EnableTransactionManagement
@EnableCaching

public class LojaApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(LojaApplication.class, args);	
		//System.out.println(new BCryptPasswordEncoder().encode("Decantador@01"));
	}

/* 
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LojaApplication.class);
	}
*/

 // spring boot 2.x
 @Bean
 public ServletWebServerFactory servletContainer() {
	 TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
		 @Override
		 protected void postProcessContext(Context context) { 
			 SecurityConstraint securityConstraint = new SecurityConstraint();
			 securityConstraint.setUserConstraint("CONFIDENTIAL");
			 SecurityCollection collection = new SecurityCollection();
			 collection.addPattern("/*");
			 securityConstraint.addCollection(collection);
			 context.addConstraint(securityConstraint);
		 }
	 };
	 tomcat.addAdditionalTomcatConnectors(redirectConnector());
	 return tomcat;
 }

 private Connector redirectConnector() {
	 Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	 connector.setScheme("http");
	 connector.setPort(80);
	 connector.setSecure(false);
	 connector.setRedirectPort(443);

	 return connector;
 }

}
