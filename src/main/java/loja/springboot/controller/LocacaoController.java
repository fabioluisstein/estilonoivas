package loja.springboot.controller;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

import loja.springboot.model.Cliente;
import loja.springboot.model.Locacao;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.model.Parcela;
import loja.springboot.model.Produto;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.CidadeRepository;
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
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ReportUtil reportUtil;

	private ModelAndView andViewLista = new ModelAndView("locacao/lista");
	private ModelAndView andViewCadastro = new ModelAndView("locacao/cadastrolocacao");
 
	/*Ajustar cache na cidade e cliente */

	@RequestMapping(method = RequestMethod.GET, value = "/listalocacoes")
	public ModelAndView locacoes() {
		andViewLista.addObject("locacoes", locacaoRepository.top120Locacao());
		Runtime.getRuntime().gc();
		return andViewLista;
	
	}
	 
	@PostMapping("/pesquisarlocacao")
	public ModelAndView pesquisar(@RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal)  {
	
		if(dataInicio.isEmpty() && dataFinal.isEmpty()) {
			andViewLista.addObject("locacoes", locacaoRepository.findAllTodos());
		} 
		 
		if(!dataInicio.isEmpty() && !dataFinal.isEmpty()) {	
			andViewLista.addObject("locacoes", locacaoRepository.findLocacaoDatas(dataInicio,dataFinal));
			return andViewLista;
		}
		
		andViewLista.addObject("locacoes", locacaoRepository.findAllTodos());
		return andViewLista;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "cadastrolocacao")
	public ModelAndView cadastro(Locacao locacao) {
		locacao.setData_locacao(new Date());
		andViewCadastro.addObject("locacaobj", locacao);
		andViewCadastro.addObject("parcelabj", new Parcela());
		andViewCadastro.addObject("produtobj", new LocacaoProduto());
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("clientes", clienteRepository.findAll());
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		return andViewCadastro;
	}
	

	@RequestMapping(method = RequestMethod.GET, value = "locacoesVencidas")
	public ModelAndView locacoesVencidas() {	
		andViewLista.addObject("locacoes", locacaoRepository.locacoesVencidas());
		return andViewLista;
	}
	

	@GetMapping("/cadastrolocacao/{idCliente}")
	public ModelAndView cadastroLocacaoCLiente(Locacao locacao, @PathVariable("idCliente") Cliente cliente) {
		locacao.setData_locacao(new Date());
		locacao.setCliente(cliente);
		andViewCadastro.addObject("locacaobj", locacao);
		andViewCadastro.addObject("parcelabj", new Parcela());
		andViewCadastro.addObject("produtobj", new LocacaoProduto());
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("clientes", cliente);
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		return andViewCadastro;
	}
	
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarlocacao")
	public String salvar(Locacao locacao) throws IOException {	
	locacaoRepository.save(locacao);
	  return "redirect:/voltar/"+locacao.getId().toString()+"";	
	} 

	
	@GetMapping("/editarparcela/{idparcela}")
	public ModelAndView editarParcela(@PathVariable("idparcela") Parcela parcela)  {
		andViewCadastro.addObject("locacaobj",parcela);
		andViewCadastro.addObject("produtobj", new LocacaoProduto());
		andViewCadastro.addObject("parcelabj", parcela);	
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("clientes", clienteRepository.findAll());
		andViewCadastro.addObject("produtos", produtoRepository.findAll());
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andViewCadastro.addObject("totalProdutos",parcela.getLocacao().getValorTotalProdutos());
		andViewCadastro.addObject("totalPagamento",parcela.getLocacao().getValorTotal());
		return andViewCadastro;
	}
	
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarparcela")
	public String salvarParcela(Parcela parcela) throws IOException {	
		 parcelaRepository.saveAndFlush(parcela);
		    return "redirect:/voltar/"+parcela.getLocacao().getId().toString()+"";
	} 
	
	
	@GetMapping("/editarprodutolocacao/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") LocacaoProduto locacaoProduto)  {
		andViewCadastro.addObject("locacaobj",locacaoProduto.getLocacao());
		andViewCadastro.addObject("produtobj", locacaoProduto);
		andViewCadastro.addObject("parcelabj", new Parcela());	
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("produtos", produtoRepository.findAll());
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andViewCadastro.addObject("totalProdutos",locacaoProduto.getLocacao().getValorTotalProdutos());
		andViewCadastro.addObject("totalPagamento",locacaoProduto.getLocacao().getValorTotal());
		return andViewCadastro;
	} 
	

	@GetMapping("/gerarRelatorio/{idlocacao}")
	public void imprimePdf(@PathVariable("idlocacao") Long idlocacao, 
			
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
      Map<String,Object> paramMap = new HashMap<String, Object>();
      paramMap.put("idLocacao", idlocacao.toString());//Aqui vc passa os parâmetros para um hashmap, que será enviado para o relatório
      
		byte[] pdf = reportUtil.gerarRelatorio( "contrato",paramMap, request.getServletContext());
		response.setContentLength(pdf.length);
		// envia a resposta com o MIME Type
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		response.getOutputStream().write(pdf);
		
	} 
	
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarproduto")
	public String salvarProduto(LocacaoProduto produtoLocacao) throws IOException {	
		 locacaoProdutoRepository.saveAndFlush(produtoLocacao);
		    return "redirect:/voltar/"+produtoLocacao.getLocacao().getId().toString()+"";
	} 
	
	@GetMapping("/voltar/{idlocacao}")
	public ModelAndView voltar(@PathVariable("idlocacao") Locacao locacao)  {
		Parcela parcela = new Parcela(locacao);
		LocacaoProduto locacaoProduto = new LocacaoProduto(locacao); 
	    andViewCadastro.addObject("locacaobj",locacao);
		andViewCadastro.addObject("locacaoId",locacao.getId());
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("parcelabj", parcela);
		andViewCadastro.addObject("produtobj", locacaoProduto);
		andViewCadastro.addObject("clientes", clienteRepository.findAll());
		andViewCadastro.addObject("parcelas", locacao.getParcelas());
		andViewCadastro.addObject("produtosLocacoes", locacao.getProdutos());	
		andViewCadastro.addObject("produtos", produtoRepository.findAll());
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andViewCadastro.addObject("totalProdutos",locacao.getValorTotalProdutos());
		andViewCadastro.addObject("totalPagamento",locacao.getValorTotal());
		return andViewCadastro;
	}
	
	@GetMapping("/iniciaLocao/{idlocacao}")
	public ModelAndView iniciaLocao(@PathVariable("idlocacao") Locacao locacao)  {
		Parcela parcela = new Parcela( locacao);
		LocacaoProduto locacaoProduto = new LocacaoProduto(locacao);
	    andViewCadastro.addObject("locacaobj",locacao);
		andViewCadastro.addObject("locacaoId",locacao.getId());
		andViewCadastro.addObject("colaboradores", colaboradorRepository.findAll());
		andViewCadastro.addObject("parcelabj", parcela);
		andViewCadastro.addObject("produtobj", locacaoProduto);
		andViewCadastro.addObject("clientes", clienteRepository.findAll());
		andViewCadastro.addObject("parcelas", locacao.getParcelas());
		andViewCadastro.addObject("produtosLocacoes", locacao.getProdutos());	
		andViewCadastro.addObject("produtos", produtoRepository.findAll());
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		andViewCadastro.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andViewCadastro.addObject("totalProdutos",locacao.getValorTotalProdutos());
		andViewCadastro.addObject("totalPagamento",locacao.getValorTotal());
		return andViewCadastro;
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
	
	
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@GetMapping("/removerlocacao/{idlocacao}")
	public String excluir(@PathVariable("idlocacao") Long idlocacao) {
		locacaoRepository.deleteById(idlocacao);	
		return "redirect:/listalocacoes";
	}
	 
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@GetMapping("/removerparcela/{idparcela}")
	public String excluirParcela(@PathVariable("idparcela") Long idparcela) {
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		parcelaRepository.deleteById(idparcela);	
		return "redirect:/voltar/"+parcela.getLocacao().toString();
	}
	
	@CacheEvict(value={"locacoes120","listParcelasMesAtual"} , allEntries=true)
	@GetMapping("/removereprodutolocacao/{idproduto}")
	public String excluirProduto(@PathVariable("idproduto") Long idproduto) {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findById(idproduto).get();
		locacaoProdutoRepository.deleteById(idproduto);	
		return "redirect:/voltar/"+locacaoProduto.getLocacao().toString();
	}
	
	
}