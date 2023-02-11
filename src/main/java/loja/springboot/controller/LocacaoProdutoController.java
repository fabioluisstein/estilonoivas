package loja.springboot.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Locacao;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.model.Parcela;
import loja.springboot.repository.LocacaoProdutoRepository;
import loja.springboot.repository.LocacaoRepository;
import loja.springboot.repository.ProdutoRepository;

@Controller
public class LocacaoProdutoController {
 

	@Autowired
	private LocacaoRepository locacaoRepository;
	
	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	 
	@GetMapping("/editarprodutolocacaoCustoms/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") Long idproduto)  {
		Optional<LocacaoProduto> locacaoProduto = locacaoProdutoRepository.findById(idproduto);
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("locacaobj",locacaoProduto.get().getLocacao());
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("produtos", produtoRepository.findAll());
		return andView;
	} 

	@GetMapping("/editarprodutolocacaoCustom/{idproduto}")
	public ModelAndView edit(@PathVariable("idproduto") Long idproduto)  {
		ModelAndView andView = new ModelAndView("/listaAjustes");

    return andView;
	} 

	@GetMapping("/liberarProduto/{idprodutoLocacao}")
	public ModelAndView liberacaoProduto(@PathVariable("idprodutoLocacao") Long idprodutoLocacao)  {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.getById(idprodutoLocacao);
		locacaoProduto.setData_liberacao(new Date());
		locacaoProdutoRepository.saveAndFlush(locacaoProduto);
		ModelAndView andView = new ModelAndView("produto/ajustes");
		andView.addObject("produtosContrato", locacaoProdutoRepository.locacoesProdutos()); 
		
    return andView;
	} 
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarprodutoCustom")
	public String salvarProdutoCustom(LocacaoProduto produtoLocacao) throws IOException {	
		 locacaoProdutoRepository.saveAndFlush(produtoLocacao);
		    return "redirect:/voltar/"+produtoLocacao.getLocacao().getId().toString()+"";
	} 
	
	@GetMapping("/voltarCustom/{idlocacao}")
	public ModelAndView voltar(@PathVariable("idlocacao") Long idlocacao)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		Optional<Locacao> locacao = locacaoRepository.findById(idlocacao);
		Parcela parcela = new Parcela();
		LocacaoProduto locacaoProduto = new LocacaoProduto();
		locacaoProduto.setLocacao(locacao.get());
	    parcela.setLocacao(locacao.get()); 
	    andView.addObject("locacaobj",locacao);
		andView.addObject("parcelabj", parcela);
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("parcelas", locacao.get().getParcelas());
		andView.addObject("produtosLocacoes", locacao.get().getProdutos());	
		andView.addObject("produtos", produtoRepository.findAll());	
		return andView;
	}
	
	


	
	
	

	
	
}