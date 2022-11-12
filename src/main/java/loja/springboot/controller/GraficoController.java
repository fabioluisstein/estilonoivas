package loja.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import loja.springboot.model.Grafico;
import loja.springboot.model.Pagamento;
import loja.springboot.model.Parcela;
import loja.springboot.repository.PagamentoRepository;
import loja.springboot.repository.ParcelaRepository;

@Controller
public class GraficoController {
 


	@Autowired
	private ParcelaRepository parcelaRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@GetMapping(value = "/buscargrafico") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public void buscargraficoid( HttpServletResponse response) throws IOException { 
		List<String> meses = new ArrayList<String>();
	

		Calendar c = Calendar.getInstance();

				meses.add(""+c.get(Calendar.MONTH));
		
		
		
		List<Double> entradas = new ArrayList<Double>();
		
		List<Double> pagamentos = new ArrayList<Double>();
	
		
		List<Pagamento> listPag = pagamentoRepository.pagamentoMesAtual();
		
		Double valor = 0.00;
		for(Pagamento pagamento: listPag) {  
			valor = valor + pagamento.getValor();
		}
		
		pagamentos.add(valor);
		
		Double valorParcela = 0.00;
	    List<Parcela> listPar = parcelaRepository.parcelaMesAtual();
		
		for(Parcela parcela: listPar) {  
			valorParcela = valorParcela + parcela.getValor();
		}
		
		entradas.add(valorParcela);
			
		Grafico grafico = new Grafico(); 
		
		grafico.setMeses(meses);
		grafico.setEntradas(entradas);
		grafico.setPagamentos(pagamentos);
		
		 ObjectMapper mapper = new ObjectMapper();
		 String json = mapper.writeValueAsString(grafico);
		 response.getWriter().write(json);
	
	}   
	
	
	
	
}