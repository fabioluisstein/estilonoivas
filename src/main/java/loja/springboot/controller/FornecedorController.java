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

import loja.springboot.model.Fornecedor;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.FornecedorRepository;

@Controller
public class FornecedorController {
 

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
 
	@Cacheable("fornecedores") 
	@RequestMapping(method = RequestMethod.GET, value = "/listafornecedores")
	public ModelAndView fornecedores() {
		ModelAndView andView = new ModelAndView("fornecedor/lista");
		andView.addObject("fornecedores", fornecedorRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarfornecedor")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("fornecedor/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("fornecedores", fornecedorRepository.top10());
		}
		modelAndView.addObject("fornecedores", fornecedorRepository.findFornecedorByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@Cacheable("fornecedores")  
	@RequestMapping(method = RequestMethod.GET, value = "cadastrofornecedor")
	public ModelAndView cadastro(Fornecedor fornecedor) {
		ModelAndView modelAndView = new ModelAndView("fornecedor/cadastrofornecedor");
		modelAndView.addObject("fornecedorbj", new Fornecedor());
		modelAndView.addObject("cidades", cidadeRepository.findAll());
		return modelAndView;
	}
	
	@CacheEvict(value="fornecedores",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarfornecedor")
	public ModelAndView salvar(Fornecedor fornecedor) {
		ModelAndView andView = new ModelAndView("fornecedor/cadastrofornecedor");
		andView.addObject("cidades", cidadeRepository.findAll());
		andView.addObject("fornecedorbj",fornecedorRepository.saveAndFlush(fornecedor));
		return andView;
	}
	
	@GetMapping("/editarfornecedor/{idfornecedor}")
	public ModelAndView editar(@PathVariable("idfornecedor") Long idfornecedor) {
		Optional<Fornecedor> fornecedor = fornecedorRepository.findById(idfornecedor);
		
		return salvar(fornecedor.get());
	}
	
	@CacheEvict(value="fornecedores",allEntries=true)
	@GetMapping("/removerfornecedor/{idfornecedor}")
	public ModelAndView excluir(@PathVariable("idfornecedor") Long idfornecedor) {
		fornecedorRepository.deleteById(idfornecedor);	
		ModelAndView andView = new ModelAndView("fornecedor/lista");
		andView.addObject("fornecedores", fornecedorRepository.top10());
		return andView;
	}
	
}