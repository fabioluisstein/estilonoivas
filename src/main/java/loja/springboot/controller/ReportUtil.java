package loja.springboot.controller;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportUtil implements Serializable {

	

	private static final long serialVersionUID = 1L;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public byte[] gerarRelatorio (String nomeRelatorio, Map<String,Object> params , ServletContext servletContext) throws Exception {
		
		
		/*Obter a conexão com o banco de dados*/
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		/*Carregar o caminho do arquivo Jasper*/
		
		ClassPathResource reportResource = new ClassPathResource("relatorios/contrato.jasper");
	    JasperPrint jasperPrint = JasperFillManager.fillReport(reportResource.getInputStream(), params, connection);
        	
    	/*Exporta para byte o PDF para fazer o download*/
    	byte [] retorno = JasperExportManager.exportReportToPdf(jasperPrint);
    		
    	connection.close();
    		
   
		return retorno;
	}

}
