package loja.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.repository.LocacaoProdutoRepository;

@Controller
public class AjusteController {

	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaAjustes")
	public ModelAndView produtos() {
		ModelAndView andView = new ModelAndView("produto/ajustes");
		andView.addObject("produtosContrato", locacaoProdutoRepository.locacoesProdutos()); 
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return andView;
	}
	 
	@PostMapping("/pesquisarprodutoId")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") Long nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("produto/ajustes");
		if(nomepesquisa==null) {
			modelAndView.addObject("produtos", locacaoProdutoRepository.locacoesProdutos());
		}
		modelAndView.addObject("produtos", locacaoProdutoRepository.findProdutoById(nomepesquisa));
		return modelAndView;
	}
	
	@GetMapping("/listarItensContrato/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") Long idproduto)  {
		ModelAndView andView = new ModelAndView("produto/ajustes");
		andView.addObject("produtoTeste",idproduto);
		return andView;
	} 
	


	
	
}