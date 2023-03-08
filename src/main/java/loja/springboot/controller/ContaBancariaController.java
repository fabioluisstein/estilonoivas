package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.ContaBancaria;
import loja.springboot.repository.ContaBancariaRepository;
import loja.springboot.repository.ParcelaRepository;

@Controller
public class ContaBancariaController {
 

	@Autowired
	private ContaBancariaRepository contaBancariaRepository;

	
	@Autowired
	private ParcelaRepository parcelaRepository;
 
 
    @Cacheable("contas") 
	@RequestMapping(method = RequestMethod.GET, value = "/listacontasBancarias")
	public ModelAndView listacontasBancarias() {
		ModelAndView andView = new ModelAndView("conta/lista");
		andView.addObject("listacontasBancarias", contaBancariaRepository.top10());
		return andView;
	}
	 


	@RequestMapping(method = RequestMethod.GET, value = "/listaParcelas")
	public ModelAndView listaPagamentos() {
		ModelAndView andView = new ModelAndView("conta/listaParcelas");
		andView.addObject("parcelas", parcelaRepository.parcelaMesAtual()); 
		return andView;
	}
	 


	@Cacheable("contas") 
	@PostMapping("/pesquisarcontas")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("conta/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("contas", contaBancariaRepository.top10());
		}
		modelAndView.addObject("contas", contaBancariaRepository.findContaByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastroConta")
	public ModelAndView cadastroConta(ContaBancaria conta) {
		ModelAndView modelAndView = new ModelAndView("conta/cadastroconta");
		modelAndView.addObject("contabj", new ContaBancaria());
		return modelAndView;
	}
	
	@CacheEvict(value="contas",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarconta")
	public ModelAndView salvarConta(ContaBancaria conta) { 
		ModelAndView andView = new ModelAndView("conta/cadastroconta");
		andView.addObject("contabj",contaBancariaRepository.saveAndFlush(conta));
		return andView;
	}
	
	@CacheEvict(value="contas",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarConta")
	public ModelAndView salvar(ContaBancaria conta) { 
		ModelAndView andView = new ModelAndView("conta/cadastroconta");
		andView.addObject("contabj",contaBancariaRepository.saveAndFlush(conta));
		return andView;
	}

	@GetMapping("/editarconta/{idconta}")
	public ModelAndView editar(@PathVariable("idconta") Long idconta) {
		Optional<ContaBancaria> conta = contaBancariaRepository.findById(idconta);
		return salvar(conta.get());
	}
	
	@CacheEvict(value="contas",allEntries=true)
	@GetMapping("/removerconta/{idconta}")
	public String excluir(@PathVariable("idconta") Long idconta) {
		contaBancariaRepository.deleteById(idconta);	
		return "redirect:/listacontasBancarias/";
		
	}
	
}