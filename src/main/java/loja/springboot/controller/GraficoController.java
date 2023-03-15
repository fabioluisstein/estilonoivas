package loja.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import loja.springboot.dto.GraficoDTO;
import loja.springboot.model.Grafico;
import loja.springboot.repository.GraficoRepository;

@Controller
public class GraficoController {
 

	@Autowired
	private GraficoRepository graficoRepository;
	
	
	@RequestMapping("/index2")
	public String index2() {
		return "index2.html";
	}
	
	
	@GetMapping(value = "/buscargrafico") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public void buscargraficoid( HttpServletResponse response) throws IOException { 
		
	    List<GraficoDTO> grafico = new ArrayList<GraficoDTO>();
	    grafico = graficoRepository.findAll();
	    
	    Grafico graf = new Grafico();
	 
	    List<Double>  entradas = new ArrayList<Double>();
	    List<Double>  saidas = new ArrayList<Double>();
	    List<String>  meses = new ArrayList<String>();
	    
	    meses.add(grafico.get(0).getMes().toString());
	    entradas.add(grafico.get(0).getEntrada());
	    saidas.add(grafico.get(0).getDespesa());
	 
	    graf.setMeses(meses);
	    graf.setEntradas(entradas);
	    graf.setPagamentos(saidas);
	    
		
		 ObjectMapper mapper = new ObjectMapper();
		 String json = mapper.writeValueAsString(graf);
		 response.getWriter().write(json);
	
	}   
	
	
	
	
}