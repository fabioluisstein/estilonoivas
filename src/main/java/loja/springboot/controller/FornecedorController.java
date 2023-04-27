package loja.springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 
	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listafornecedores")
	public ModelAndView fornecedores() {
		ModelAndView andView = new ModelAndView("fornecedor/lista");
		andView.addObject("fornecedores", fornecedorRepository.fornecedoresTodos());
		garbageCollection();
		return andView;
	}
	 
	@RequestMapping(method = RequestMethod.GET, value = "cadastrofornecedor")
	public ModelAndView cadastro(Fornecedor fornecedor) {
		ModelAndView modelAndView = new ModelAndView("fornecedor/cadastrofornecedor");
		modelAndView.addObject("fornecedorbj", new Fornecedor());
		modelAndView.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		garbageCollection();
		return modelAndView;
	}
	
	@CacheEvict(value={"forncedoresTodosDto", "saidas", "pagamentosTodos", "listProdutos"} , allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarfornecedor")
	public ModelAndView salvar(Fornecedor fornecedor) {
		ModelAndView andView = new ModelAndView("fornecedor/cadastrofornecedor");
		andView.addObject("cidades", cidadeRepository.findAll());
		andView.addObject("fornecedorbj",fornecedorRepository.saveAndFlush(fornecedor));
		garbageCollection();
		return andView;
	}
	
	@GetMapping("/editarfornecedor/{idfornecedor}")
	public ModelAndView editar(@PathVariable("idfornecedor") Fornecedor fornecedor) {
		ModelAndView andView = new ModelAndView("fornecedor/cadastrofornecedor");
		andView.addObject("fornecedorbj",fornecedor);
		andView.addObject("cidades", cidadeRepository.findAll());
		garbageCollection();
		return andView;
	}
	
	@CacheEvict(value={"forncedoresTodosDto", "saidas", "pagamentosTodos", "listProdutos"} , allEntries=true)
	@GetMapping("/removerfornecedor/{idfornecedor}")
	public String excluir(@PathVariable("idfornecedor") Long idfornecedor) {
		try {
		   fornecedorRepository.deleteById(idfornecedor);	
	    } catch (Exception e) {
	     }
	return "redirect:/listafornecedores";
  } 
	
	
}