package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
import loja.springboot.repository.ParcelaRepository;

@Controller
public class ContaBancariaController {
 
	@Autowired
	private ContaBancariaRepository contaBancariaRepository;
	@Autowired
	private ParcelaRepository parcelaRepository;
 
	@RequestMapping(method = RequestMethod.GET, value = "/listacontasBancarias")
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
		ModelAndView modelAndView = new ModelAndView("conta/cadastroconta");
		modelAndView.addObject("contabj", new ContaBancaria());
		return modelAndView;
	}
	
	@CacheEvict(value="contasBancariasTodas",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarconta")
	public ModelAndView salvarConta(ContaBancaria conta) { 
		ModelAndView andView = new ModelAndView("conta/cadastroconta");
		andView.addObject("contabj",contaBancariaRepository.saveAndFlush(conta));
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
	public ModelAndView editar(@PathVariable("idconta") Long idconta) {
		Optional<ContaBancaria> conta = contaBancariaRepository.findById(idconta);
		return salvarConta(conta.get());
	}
	
	@CacheEvict(value="contasBancariasTodas",allEntries=true)
	@GetMapping("/removerconta/{idconta}")
	public String excluir(@PathVariable("idconta") Long idconta) {
		contaBancariaRepository.deleteById(idconta);	
		return "redirect:/listacontasBancarias/";
		
	}
	
}