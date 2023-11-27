package loja.springboot.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.ContaBancaria;
import loja.springboot.model.Pessoa;
import loja.springboot.repository.ContaBancariaRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.repository.ParcelaRepository;
import loja.springboot.service.ContaBancariaService;

@Controller
public class ContaBancariaController {
 
	@Autowired
	private ContaBancariaRepository contaBancariaRepository;
	@Autowired
	private ParcelaRepository parcelaRepository;

	@Autowired
	private PainelRepository painelRepository;
 
	@RequestMapping(method = RequestMethod.GET, value = "/listacontasBancariass")
	public ModelAndView listacontasBancarias() {
		ModelAndView andView = new ModelAndView("conta/lista");
		Pessoa p = new Pessoa();
        if( p.obterUsuarioLogado().equalsIgnoreCase("adm") ){
			andView.addObject("listacontasBancarias", contaBancariaRepository.listContasBancarias());
		} 	
		return andView;
	}
	 
	@RequestMapping(method = RequestMethod.GET, value = "/listaParcelas")
	public ModelAndView listaPagamentos() {
		ModelAndView andView = new ModelAndView("conta/listaParcelas");
		andView.addObject("parcelas", parcelaRepository.parcelaMesAtual()); 
		return andView;
	}
	 
	@RequestMapping(method = RequestMethod.GET, value = "cadastroConta")
	public ModelAndView cadastroConta(ContaBancaria conta) {
		ModelAndView modelAndView = new ModelAndView("conta/cadastrocontas");
		modelAndView.addObject("id", "Cadastrando Conta");
		modelAndView.addObject("contabj", new ContaBancaria());
		return modelAndView;
	}
	
	@CacheEvict(value="contasBancariasTodas",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarconta")
	public ModelAndView salvarConta(ContaBancaria conta) { 
		ModelAndView andView = new ModelAndView("conta/cadastrocontas");
		andView.addObject("contabj",contaBancariaRepository.saveAndFlush(conta));
		andView.addObject("id", "Gravado com Sucesso");
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "parcelasProblemas")
	public ModelAndView parcelasProblemas() {	
		ModelAndView andView = new ModelAndView("conta/listaParcelas");
		andView.addObject("parcelas", parcelaRepository.parcelaMesProblemas());
		garbageCollection(); 
		return andView;
	}

	
	@PostMapping("/pesquisarparcela")
	public ModelAndView pesquisar(@RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal)  {
	  
		ModelAndView andView = new ModelAndView("conta/listaParcelas");
		if(dataInicio.isEmpty() && dataFinal.isEmpty()) {
			andView.addObject("parcelas", parcelaRepository.parcelaMesAtual()); 
		} 
		 
		if(!dataInicio.isEmpty() && !dataFinal.isEmpty()) {	
			andView.addObject("parcelas", parcelaRepository.findLocacaoDatas(dataInicio,dataFinal));
			return andView;
		}
		
		andView.addObject("parcelas", parcelaRepository.parcelaMesAtual());
		garbageCollection(); 
		return andView;
	}


	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}


	@GetMapping("/editarconta/{idconta}")
	public ModelAndView editar(@PathVariable("idconta") ContaBancaria conta) {
		ModelAndView andView = new ModelAndView("conta/cadastrocontas");
		andView.addObject("id", "Editando Registro");
		andView.addObject("contabj",contaBancariaRepository.saveAndFlush(conta));
		return andView; 
	}
	
	@CacheEvict(value="contasBancariasTodas",allEntries=true)
	@GetMapping("/removerconta/{idconta}")
	public String excluir(@PathVariable("idconta") Long idconta) {
		contaBancariaRepository.deleteById(idconta);	
		return "redirect:/listacontasBancarias/";
		
	}

	@GetMapping("/listacontasBancarias")
		public ModelAndView showTabela2() {
		List<listPainelOperacional> grafico = painelRepository.grafico();
	    ModelAndView andView = new ModelAndView("conta/contas-datatable");
		andView.addObject("qtdLocacao", grafico.get(0).getLocacoes()); 
		andView.addObject("ticket", grafico.get(0).getTicket());
		andView.addObject("indicadorGeral", grafico.get(0).getIndice());
		andView.addObject("locadoHoje", grafico.get(0).getLocado());
		return andView;	
		}

	    @GetMapping("/listcontasBancarias")
		public ModelAndView showTabela3() {
		
	    ModelAndView andView = new ModelAndView("conta/cadastrocontas");
				     andView.addObject("contabj", new ContaBancaria());
		return andView;	
		}

   @GetMapping("/serverContas")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new ContaBancariaService().execute(contaBancariaRepository, request);
			return ResponseEntity.ok(data);
	}
	
}