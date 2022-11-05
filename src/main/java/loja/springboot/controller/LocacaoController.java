package loja.springboot.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.tomcat.util.codec.binary.Base64;
import loja.springboot.model.Locacao;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.model.Parcela;
import loja.springboot.model.Produto;
import loja.springboot.repository.ClienteRepository;
import loja.springboot.repository.LocacaoProdutoRepository;
import loja.springboot.repository.LocacaoRepository;
import loja.springboot.repository.ParcelaRepository;
import loja.springboot.repository.PessoaRepository;
import loja.springboot.repository.ProdutoRepository;

@Controller
public class LocacaoController {
 

	@Autowired
	private PessoaRepository colaboradorRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private LocacaoRepository locacaoRepository;
	
	@Autowired
	private ParcelaRepository parcelaRepository;
	
	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ReportUtil reportUtil;
 
	@Cacheable("locacoes") 
	@RequestMapping(method = RequestMethod.GET, value = "/listalocacoes")
	public ModelAndView locacoes() {
		ModelAndView andView = new ModelAndView("locacao/lista");
		andView.addObject("locacoes", locacaoRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarlocacao")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, 
			                      @RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal)  {
	  
	  ModelAndView modelAndView = new ModelAndView("locacao/lista");
		if(nomepesquisa.isEmpty() && dataInicio.isEmpty() && dataFinal.isEmpty()) {
			modelAndView.addObject("locacoes", locacaoRepository.top10());
		}
		
		if(!dataInicio.isEmpty() && !dataFinal.isEmpty()) {	
			modelAndView.addObject("locacoes", locacaoRepository.findLocacaoDatas(dataInicio,dataFinal));
			return modelAndView;
		}
		
		modelAndView.addObject("locacoes", locacaoRepository.findLocacaoByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}
	
	@Cacheable("locacoes")  
	@RequestMapping(method = RequestMethod.GET, value = "cadastrolocacao")
	public ModelAndView cadastro(Locacao locacao) {
		locacao.setData_locacao(new Date());
		ModelAndView modelAndView = new ModelAndView("locacao/cadastrolocacao");
		modelAndView.addObject("locacaobj", locacao);
		modelAndView.addObject("parcelabj", new Parcela());
		modelAndView.addObject("produtobj", new LocacaoProduto());
		modelAndView.addObject("colaboradores", colaboradorRepository.findAll());
		modelAndView.addObject("clientes", clienteRepository.findAll());
		return modelAndView;
	}
	
	@CacheEvict(value="locacoes",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarlocacao")
	public String salvar(Locacao locacao) throws IOException {	
	locacaoRepository.saveAndFlush(locacao);
	  return "redirect:/voltar/"+locacao.getId().toString()+"";	
	} 
	
	
	
	@GetMapping("/editarparcela/{idparcela}")
	public ModelAndView editarParcela(@PathVariable("idparcela") Long idparcela)  {
		Optional<Parcela> parcela = parcelaRepository.findById(idparcela);
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("locacaobj",parcela.get().getLocacao());
		andView.addObject("produtobj", new LocacaoProduto());
		andView.addObject("parcelabj", parcela);	
		andView.addObject("produtos", produtoRepository.findAll());
		return andView;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarparcela")
	public String salvarParcela(Parcela parcela) throws IOException {	
		 parcelaRepository.saveAndFlush(parcela);
		    return "redirect:/voltar/"+parcela.getLocacao().getId().toString()+"";
	} 
	
	
	@GetMapping("/editarprodutolocacao/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") Long idproduto)  {
		Optional<LocacaoProduto> locacaoProduto = locacaoProdutoRepository.findById(idproduto);
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("locacaobj",locacaoProduto.get().getLocacao());
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("parcelabj", new Parcela());	
		andView.addObject("produtos", produtoRepository.findAll());
		return andView;
	} 
	

	@GetMapping("/gerarRelatorio/{idlocacao}")
	public void imprimePdf(@PathVariable("idlocacao") Long idlocacao, 
			
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
      Map<String,Object> paramMap = new HashMap<String, Object>();
      paramMap.put("idLocacao", idlocacao.toString());//Aqui vc passa os parâmetros para um hashmap, que será enviado para o relatório

		byte[] pdf = reportUtil.gerarRelatorio( "contrato",paramMap, request.getServletContext());
		

		response.setContentLength(pdf.length);
		

		response.setContentType("application/octet-stream");
		
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		
		response.getOutputStream().write(pdf);
		
	} 
	
	
	
	
	
	
	@PostMapping(value="/gerarRelatorio/{idlocacao}", produces = "application/text")
	public ResponseEntity<String> downloadRelatorioParam(@PathVariable("idlocacao") Long idlocacao, HttpServletRequest request) throws Exception {
				
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("idLocacao", idlocacao.toString());//Aqui vc passa os parâmetros para um hashmap, que será enviado para o relatório
		
		byte[] pdf = reportUtil.gerarRelatorio( "contrato",params, request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarproduto")
	public String salvarProduto(LocacaoProduto produtoLocacao) throws IOException {	
		 locacaoProdutoRepository.saveAndFlush(produtoLocacao);
		    return "redirect:/voltar/"+produtoLocacao.getLocacao().getId().toString()+"";
	} 
	
	
	@GetMapping("/voltar/{idlocacao}")
	public ModelAndView voltar(@PathVariable("idlocacao") Long idlocacao)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		Optional<Locacao> locacao = locacaoRepository.findById(idlocacao);
		Parcela parcela = new Parcela();
		LocacaoProduto locacaoProduto = new LocacaoProduto();
		locacaoProduto.setLocacao(locacao.get());
	    parcela.setLocacao(locacao.get());
	    andView.addObject("locacaobj",locacao);
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("parcelabj", parcela);
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("clientes", clienteRepository.findAll());
		andView.addObject("parcelas", locacao.get().getParcelas());
		andView.addObject("produtosLocacoes", locacao.get().getProdutos());	
		andView.addObject("produtos", produtoRepository.findAll());	
		return andView;
	}
	
	
	
	
	
	@GetMapping("/iniciaLocao/{idlocacao}")
	public ModelAndView iniciaLocao(@PathVariable("idlocacao") Long idlocacao)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		Optional<Locacao> locacao = locacaoRepository.findById(idlocacao);
		Parcela parcela = new Parcela();
		LocacaoProduto locacaoProduto = new LocacaoProduto();
		locacaoProduto.setLocacao(locacao.get());
	    parcela.setLocacao(locacao.get());
	    andView.addObject("locacaobj",locacao);
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("parcelabj", parcela);
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("clientes", clienteRepository.findAll());
		andView.addObject("parcelas", locacao.get().getParcelas());
		andView.addObject("produtosLocacoes", locacao.get().getProdutos());	
		andView.addObject("produtos", produtoRepository.findAll());	
		return andView;
	}
	
	
	

	
	@GetMapping("/editarlocacao/{idlocacao}")
	public String editar(@PathVariable("idlocacao") Long idlocacao)  {
	    return "redirect:/iniciaLocao/"+idlocacao.toString()+"";
	}
	

	@GetMapping(value = "/buscarparcelaid") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public ResponseEntity<Parcela> buscarparcelaid(@RequestParam(name = "idparcela") Long idparcela) { 
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		return new ResponseEntity<Parcela>(parcela, HttpStatus.OK);
    
	}   
	
	
	
	@GetMapping(value = "/buscarprodutoid") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public ResponseEntity<Produto> buscarprodutoid(@RequestParam(name = "idproduto") Long idproduto) { 
		Produto produto = produtoRepository.findById(idproduto).get();
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    
	}   
	
	
	@CacheEvict(value="locacoes",allEntries=true)
	@GetMapping("/removerlocacao/{idlocacao}")
	public String excluir(@PathVariable("idlocacao") Long idlocacao) {
		locacaoRepository.deleteById(idlocacao);	
		return "redirect:/listalocacoes";
	}
	 
	
	@GetMapping("/removerparcela/{idparcela}")
	public String excluirParcela(@PathVariable("idparcela") Long idparcela) {
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		parcelaRepository.deleteById(idparcela);	
		return "redirect:/voltar/"+parcela.getLocacao().toString();
	}
	
	
	@GetMapping("/removereprodutolocacao/{idproduto}")
	public String excluirProduto(@PathVariable("idproduto") Long idproduto) {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findById(idproduto).get();
		locacaoProdutoRepository.deleteById(idproduto);	
		return "redirect:/voltar/"+locacaoProduto.getLocacao().toString();
	}
	
	
}