package loja.springboot.controller;

import java.text.ParseException;
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

import loja.springboot.model.Produto;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.ProdutoRepository;



@Controller
public class ProdutoController {
 

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
 
	@Cacheable("produtos") 
	@RequestMapping(method = RequestMethod.GET, value = "/listaprodutos")
	public ModelAndView produtos() {
		ModelAndView andView = new ModelAndView("produto/lista");
		andView.addObject("produtos", produtoRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarproduto")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("produto/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("produtos", produtoRepository.top10());
		}
		modelAndView.addObject("produtos", fornecedorRepository.findFornecedorByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@Cacheable("produtos")  
	@RequestMapping(method = RequestMethod.GET, value = "cadastroproduto")
	public ModelAndView cadastro(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produto/cadastroproduto");
		modelAndView.addObject("produtobj", new Produto());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		modelAndView.addObject("categorias", categoriaRepository.findAll());
		return modelAndView;
	}
	
	@CacheEvict(value="produtos",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarproduto")
	public ModelAndView salvar(Produto produto) {	
		ModelAndView andView = new ModelAndView("produto/cadastroproduto");
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findAll());
		andView.addObject("produtobj",produtoRepository.saveAndFlush(produto));
		return andView;
	} 
	
 
	@GetMapping("/editarproduto/{idproduto}")
	public ModelAndView editar(@PathVariable("idproduto") Long idproduto) throws ParseException {
		Optional<Produto> produto = produtoRepository.findById(idproduto);

		return salvar(produto.get());
	}
	
	@GetMapping("/removerproduto/{idproduto}")
	public ModelAndView excluir(@PathVariable("idproduto") Long idproduto) {
		produtoRepository.deleteById(idproduto);	
		ModelAndView andView = new ModelAndView("produto/lista");
		andView.addObject("produtos", produtoRepository.top10());
		return andView;
	}
	
}