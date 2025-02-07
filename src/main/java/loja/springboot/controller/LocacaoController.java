package loja.springboot.controller;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import loja.springboot.repository.LocacaoRepository.listEmailLocacoes;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.repository.ParcelaRepository;
import loja.springboot.repository.PessoaRepository;
import loja.springboot.repository.ProdutoRepository;
import loja.springboot.service.LocacaoDataTablesService;

@Controller
public class LocacaoController {
 
	@Autowired
	private PessoaRepository colaboradorRepository;
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	JavaMailSender sender;
	
	@Autowired
	private LocacaoRepository locacaoRepository;
	
	@Autowired
	private ParcelaRepository parcelaRepository;
	
	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	private listPainelOperacional operacional;
	
    @Autowired
	private PainelRepository painelRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ReportUtil reportUtil;

 
	/*Ajustar cache na cidade e cliente */


	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory(); 
	}

	
	@PostMapping("/pesquisarlocacao")
	public ModelAndView pesquisar(@RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal)  {
	  
	  ModelAndView modelAndView = new ModelAndView("locacao/lista");
		if(dataInicio.isEmpty() && dataFinal.isEmpty()) {
			modelAndView.addObject("locacoes", locacaoRepository.findAllTodos());
		} 
		 
		if(!dataInicio.isEmpty() && !dataFinal.isEmpty()) {	
			modelAndView.addObject("locacoes", locacaoRepository.findLocacaoDatas(dataInicio,dataFinal));
			return modelAndView;
		}
		
		modelAndView.addObject("locacoes", locacaoRepository.findAllTodos());
		garbageCollection(); 
		return modelAndView;
	}
	 
	
	@RequestMapping(method = RequestMethod.GET, value = "cadastrolocacao")
	public ModelAndView cadastro(Locacao locacao) {
		locacao.setData_locacao(new Date());
		ModelAndView modelAndView = new ModelAndView("locacao/cadastrolocacoes");
		modelAndView.addObject("locacaobj", locacao);
		modelAndView.addObject("parcelabj", new Parcela());
		modelAndView.addObject("produtobj", new LocacaoProduto());
		modelAndView.addObject("colaboradores", colaboradorRepository.findAll());
		modelAndView.addObject("clientes", clienteRepository.findAll());
		modelAndView.addObject("cidades", cidadeRepository.findAll()); 
		modelAndView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		garbageCollection(); 
		return base(modelAndView);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "locacoesVencidas")
	public ModelAndView locacoesVencidas() {	
		ModelAndView modelAndView = new ModelAndView("locacao/lista");
		modelAndView.addObject("locacoes", locacaoRepository.locacoesVencidas());
		garbageCollection(); 
		return modelAndView;
	}
	

	
	@GetMapping("cadastrolocacao/{idCliente}")
	public ModelAndView cadastroLocacaoCLiente(Locacao locacao, @PathVariable("idCliente") Cliente cliente) {
		locacao.setData_locacao(new Date());
		ModelAndView modelAndView = new ModelAndView("locacao/cadastrolocacoes");
		locacao.setCliente(cliente);
		modelAndView.addObject("locacaobj", locacao);
		modelAndView.addObject("parcelabj", new Parcela());
		modelAndView.addObject("produtobj", new LocacaoProduto());
		modelAndView.addObject("colaboradores", colaboradorRepository.findAll());
		modelAndView.addObject("clientes", cliente);
		modelAndView.addObject("cidades", cidadeRepository.listCidadades());  
		modelAndView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		garbageCollection(); 
		return base(modelAndView);
	}
	
	
	
	@GetMapping("editarparcela/{idparcela}")
	public ModelAndView editarParcela(@PathVariable("idparcela") Parcela parcela)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacoes");
		andView.addObject("locacaobj",parcela);
		andView.addObject("produtobj", new LocacaoProduto());
		andView.addObject("parcelabj", parcela);	
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("clientes", parcela.getLocacao().getCliente());
		andView.addObject("produtos", produtoRepository.findAll());
		andView.addObject("cidades", cidadeRepository.findAll()); 
		andView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andView.addObject("totalProdutos",parcela.getLocacao().getValorTotalProdutos());
		andView.addObject("totalPagamento",parcela.getLocacao().getValorTotal());
		garbageCollection(); 
		return base(andView);
	}


	
    @GetMapping("/detalhesProduto/{id}")
    @ResponseBody
    public ResponseEntity<LocacaoProduto> detalhesProduto(@PathVariable Long id) {
        LocacaoProduto produto = locacaoProdutoRepository.findById(id).orElse(null);
        return ResponseEntity.ok(produto);
    }

	@CacheEvict(value = "grafico", allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarparcela")
	public String salvarParcela(Parcela parcela) throws IOException {	
		 parcelaRepository.save(parcela);
		 garbageCollection(); 
	   return "redirect:/voltar/"+parcela.getLocacao().getId().toString()+"";
	} 
	
	
	@GetMapping("editarprodutolocacao/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") LocacaoProduto locacaoProduto)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacoes");
		andView.addObject("locacaobj",locacaoProduto.getLocacao());
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("parcelabj", new Parcela());	
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("produtos", produtoRepository.findAll());
		andView.addObject("cidades", cidadeRepository.findAll()); 
		andView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andView.addObject("totalProdutos",locacaoProduto.getLocacao().getValorTotalProdutos());
		andView.addObject("totalPagamento",locacaoProduto.getLocacao().getValorTotal());
		garbageCollection(); 
		return andView;
	} 
	
	@GetMapping("gerarRelatorio/{idlocacao}")
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
		if(locacaoRepository.findById(idlocacao).get().getEmailEnviado()==null){
		   enviaEmail(idlocacao);
	     }
		garbageCollection(); 
	} 
	
	@CacheEvict(value = "grafico", allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarproduto")
	public String salvarProduto(LocacaoProduto produtoLocacao) throws IOException {	
		 locacaoProdutoRepository.save(produtoLocacao);
		 garbageCollection(); 
	   return "redirect:/voltar/"+produtoLocacao.getLocacao().getId().toString()+"";
	} 
	
	
	@GetMapping("editarlocacao/{idlocacao}")
	public ModelAndView editarlocacao(@PathVariable("idlocacao") Locacao locacao)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacoes");
		Parcela parcela = new Parcela( locacao);
		LocacaoProduto locacaoProduto = new LocacaoProduto(locacao);
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
	    andView.addObject("locacaobj",locacao);
		andView.addObject("locacaoId",locacao.getId());
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("parcelabj", parcela);
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("clientes", locacao.getCliente());
		andView.addObject("parcelas", locacao.getParcelas());
		andView.addObject("produtosLocacoes", locacao.getProdutos());	
		andView.addObject("produtos", produtoRepository.findAll());
		andView.addObject("cidades", cidadeRepository.findAll()); 
		andView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
		andView.addObject("totalProdutos",locacao.getValorTotalProdutos());
		andView.addObject("totalPagamento",locacao.getValorTotal());
		garbageCollection(); 
		return base(andView);
	}
	
	
	@GetMapping(value = "/buscarparcelaid") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public ResponseEntity<Parcela> buscarparcelaid(@RequestParam(name = "idparcela") Long idparcela) { 
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		garbageCollection(); 
		return new ResponseEntity<Parcela>(parcela, HttpStatus.OK);
	}   
	



public void enviaEmail(Long idLocacao) throws Exception {
List<listEmailLocacoes> email = locacaoRepository.emailLocacao(idLocacao);
		try{
		if(email.size()>0) {
		String titulo = "Nova Locação Valor: "+ email.get(0).getTotal();
		String mensagem = "Locação Nº:" + email.get(0).getId() + "\n" +
		"Cliente:" + email.get(0).getCliente() + "\n" +
		"Atendente:" + email.get(0).getColaborador() + "\n" +
		"Tipo:" + email.get(0).getTipo() + "\n" +
		"Origem:" + email.get(0).getOrigem() + "\n" +
		"Cidade:" + email.get(0).getCidade() + "\n" +
		"Valor da locação:" + email.get(0).getTotal() + "\n" +
		"Valor Pago:" + email.get(0).getPago() + "\n" +
		"Valor Pendente:" + email.get(0).getPendente() + "\n" +
		"Data da Locação:" + email.get(0).getData_locacao() + "\n" +
		"Data do Evento:" + email.get(0).getData_evento() + "\n" +
		"Data da Retirada:" + email.get(0).getData_retirada() + "\n";

		SimpleMailMessage simple = new SimpleMailMessage();
		simple.setTo("dancarlos22@gmail.com");
		simple.setText(mensagem);
		simple.setSubject(titulo);
		sender.send(simple);

		Locacao locacao = locacaoRepository.findById(idLocacao).get();
		locacao.setEmailEnviado(1);
		locacaoRepository.save(locacao);
		}
}
 catch (Exception e) {
	System.out.println("erro ao enviar email");
}


}

	
	@GetMapping(value = "/buscarprodutoid") /* mapeia a url */
	@ResponseBody /* Descricao da resposta */
	public ResponseEntity<Produto> buscarprodutoid(@RequestParam(name = "idproduto") Long idproduto) { 
		Produto produto = produtoRepository.findById(idproduto).get();
		garbageCollection(); 	
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}   
	
	@CacheEvict(value = "grafico", allEntries = true)
	@GetMapping("removerlocacao/{idlocacao}")
	public String excluir(@PathVariable("idlocacao") Long idlocacao) {
		locacaoRepository.deleteById(idlocacao);	
		garbageCollection(); 
		return "redirect:/listalocacoes";
	}
	 
	@CacheEvict(value = "grafico", allEntries = true)
	@GetMapping("removerparcela/{idparcela}")
	public String excluirParcela(@PathVariable("idparcela") Long idparcela) {
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		parcelaRepository.deleteById(idparcela);
		garbageCollection(); 
		return "redirect:/editarlocacao/"+parcela.getLocacao().toString();
	}
	
	@CacheEvict(value = "grafico", allEntries = true)
	@GetMapping("removereprodutolocacao/{idproduto}")
	public String excluirProduto(@PathVariable("idproduto") Long idproduto) {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findById(idproduto).get();
		locacaoProdutoRepository.deleteById(idproduto);	
		garbageCollection(); 
		return "redirect:/editarlocacao/"+locacaoProduto.getLocacao().toString();
	}
	
	
	public void grafico() {
	  List<listPainelOperacional> grafico = painelRepository.grafico();
	        operacional = grafico.get(0);
		
		
		}

	public ModelAndView base(ModelAndView modelAndView){
	 grafico();	 modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
	 modelAndView.addObject("ticket", operacional.getTicket());
	 modelAndView.addObject("indicadorGeral", operacional.getIndice());
	 modelAndView.addObject("locadoHoje", operacional.getLocado());
	  return modelAndView;  
	}


    @GetMapping("listalocacoes")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("locacao/locacao-datatable");
		garbageCollection();
		return base(andView);	 
		} 

   
   @GetMapping("serverLocacoes")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new LocacaoDataTablesService().execute(locacaoRepository, request, 0);
			return ResponseEntity.ok(data);   
	}

	
	@GetMapping("serverLocacoesVencidas")
	public ResponseEntity<?> datatablesLocacaoVenvidas(HttpServletRequest request) {
		Map<String, Object> data = new LocacaoDataTablesService().execute(locacaoRepository, request, 1);
		return ResponseEntity.ok(data);   
}


 
@RequestMapping(method = RequestMethod.GET, value = "cadastrolocacaonew")
public ModelAndView cadastronew(Locacao locacao) {
	locacao.setData_locacao(new Date());
	ModelAndView modelAndView = new ModelAndView("locacao/cadastrolocacoes");
	modelAndView.addObject("locacaobj", locacao);
	modelAndView.addObject("parcelabj", new Parcela());
	modelAndView.addObject("produtobj", new LocacaoProduto());
	modelAndView.addObject("colaboradores", colaboradorRepository.findAll());
	modelAndView.addObject("clientes", clienteRepository.findAll());
	modelAndView.addObject("cidades", cidadeRepository.findAll()); 
	modelAndView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
	garbageCollection(); 
	return modelAndView;
}

@CacheEvict(value = "grafico", allEntries = true)
@RequestMapping(method = RequestMethod.POST, value = "salvarlocacao")
public ModelAndView salvarlocacao(Locacao locacao) throws IOException {
	Locacao loc  =  locacaoRepository.saveAndFlush(locacao);
	ModelAndView andView = new ModelAndView("locacao/cadastrolocacoes");
	Parcela parcela = new Parcela( loc);
	LocacaoProduto locacaoProduto = new LocacaoProduto(loc);
	andView.addObject("locacaobj",loc);
	andView.addObject("id", "Gravado com Sucesso");
	andView.addObject("color", "alert alert-success");
	andView.addObject("locacaoId",loc.getId());
	andView.addObject("colaboradores", colaboradorRepository.findAll());
	andView.addObject("parcelabj", parcela);
	andView.addObject("produtobj", locacaoProduto);
	andView.addObject("clientes", loc.getCliente());
	andView.addObject("parcelas", loc.getParcelas());
    andView.addObject("produtosLocacoes", loc.getProdutos());	
	andView.addObject("produtos", produtoRepository.findAll());  
	andView.addObject("cidades", cidadeRepository.findAll()); 
	andView.addObject("eventos", categoriaRepository.findCategoriaByOriginal("Evento"));
	andView.addObject("totalProdutos",loc.getValorTotalProdutos());
    andView.addObject("totalPagamento",loc.getValorTotal());
	garbageCollection(); 
	return base(andView);	
}


@CacheEvict(value = "grafico", allEntries = true)
@RequestMapping(method = RequestMethod.POST, value ="salvarprodutonew")
public String salvarProdutoNew(LocacaoProduto produtoLocacao) throws IOException {	
	 locacaoProdutoRepository.save(produtoLocacao);
	 garbageCollection(); 
   return "redirect:/editarlocacao/"+produtoLocacao.getLocacao().getId().toString()+"";
} 


@CacheEvict(value = "grafico", allEntries = true)
@RequestMapping(method = RequestMethod.POST, value ="salvarparcelanew")
public String salvarParcelaNew(Parcela parcela) throws IOException {	
	 parcelaRepository.save(parcela);
	 garbageCollection(); 
   return "redirect:/editarlocacao/"+parcela.getLocacao().getId().toString()+"";
} 







}