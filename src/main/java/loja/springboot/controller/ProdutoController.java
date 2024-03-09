package loja.springboot.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.model.Pessoa;
import loja.springboot.model.Produto;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.LocacaoProdutoRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.repository.ProdutoRepository;
import loja.springboot.service.ProdutoDataTablesService;

@Controller
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;
	private int quantidadeLocacoes;
	private double valorFinanceiro;

	private listPainelOperacional operacional;

    @Autowired
	private PainelRepository painelRepository;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	public void quantidadeLocacoes(Long id) {
		quantidadeLocacoes = 0;
		valorFinanceiro = 0;
		for (LocacaoProduto locacaoProduto : locacaoProdutoRepository.findProdutoById(id)) {
			quantidadeLocacoes = quantidadeLocacoes + 1;
			valorFinanceiro = valorFinanceiro + locacaoProduto.getValor();
		}
		garbageCollection();
	}

	public void updateVisitas(Produto produtoAcesso) throws ParseException, IOException {
		Integer quantidadeVisitas = produtoAcesso.getQuantidade_acesso();
		if (produtoAcesso.getQuantidade_acesso() == null) {
			quantidadeVisitas = 0;
		}

		produtoAcesso.setQuantidade_acesso(quantidadeVisitas + 1); 
		produtoRepository.save(produtoAcesso);
		garbageCollection();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listaprodutoss")
	public ModelAndView produtos() {
		ModelAndView andView = new ModelAndView("produto/lista");
		andView.addObject("produtos", produtoRepository.listaTodos());
		garbageCollection();
		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/consultaprodutos")
	public ModelAndView produtosPesquisa() {
		ModelAndView andView = new ModelAndView("produto/pesquisaProd");
		garbageCollection();
		return andView;
	}

	@PostMapping("/pesquisaprodutocustom")
	public ModelAndView pesquisaprodutocustom(@RequestParam("idProduto") Long idProduto) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("produto/produto");
		if (idProduto != null && !produtoRepository.findById(idProduto).isEmpty()) {
			quantidadeLocacoes(idProduto);
			Optional<Produto> produto = produtoRepository.findById(idProduto);
			updateVisitas(produto.get());
			andView.addObject("produtobj", produto);
			andView.addObject("locacoes", locacaoProdutoRepository.findProdutoLocacacoesById(idProduto));
			andView.addObject("quantidadeLocacoes", quantidadeLocacoes);
			andView.addObject("valorFinanceiro", valorFinanceiro);
			andView.addObject("fornecedores", fornecedorRepository.findAll());
			andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Produto"));
			return andView;
		}

		else {
			andView = new ModelAndView("produto/pesquisaProd");
		}
		garbageCollection();
		return andView;
	}

	@GetMapping("/consultarProdutoQr/{idproduto}")
	public ModelAndView consultar(@PathVariable("idproduto") Long idproduto) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("produto/produto");
		if (idproduto != null && !produtoRepository.findById(idproduto).isEmpty()) {
			quantidadeLocacoes(idproduto);
			Optional<Produto> produto = produtoRepository.findById(idproduto);
			updateVisitas(produto.get());  
			andView.addObject("produtobj", produto);
			andView.addObject("locacoes", locacaoProdutoRepository.findProdutoLocacacoesById(idproduto)); 
			andView.addObject("quantidadeLocacoes", quantidadeLocacoes);
			andView.addObject("valorFinanceiro", valorFinanceiro);
			andView.addObject("fornecedores", fornecedorRepository.findAll());
			andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Produto"));
			return andView;
		}

		else {
			andView = new ModelAndView("produto/pesquisaProd");
		}
		garbageCollection();
		return andView;
	
	
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastroproduto")
	public ModelAndView cadastro(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produto/cadastroprodutos");
			Pessoa p = new Pessoa();
		if (p.obterUsuarioLogado().equalsIgnoreCase("adm")) {
			modelAndView.addObject("classe", "col-md-6");
		}
		else{
           modelAndView.addObject("classe", "col-md-6 invisible");
		}
		modelAndView.addObject("produtobj", new Produto());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		modelAndView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Produto"));
		modelAndView.addObject("id", "Cadastrando Produto");
		modelAndView.addObject("color", "alert alert-dark");
		garbageCollection();
		return base(modelAndView); 
	}

	@RequestMapping(method = RequestMethod.POST, value = "salvarproduto", consumes = { "multipart/form-data" })
	public ModelAndView salvar(Produto produto, final MultipartFile file) throws IOException {
		ModelAndView andView = new ModelAndView("produto/cadastroprodutos");
			Pessoa p = new Pessoa();
		if (p.obterUsuarioLogado().equalsIgnoreCase("adm")) {
			andView.addObject("classe", "col-md-6");
		}
		else{
           andView.addObject("classe", "col-md-6 invisible");
		}

		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Produto"));
		andView.addObject("id", "Gravado com Sucesso");
		andView.addObject("color", "alert alert-success");

		if (produto.getId() == null) {
			if (file.getSize() > 0) {
				produto.setArquivo(file.getBytes());
				produto.setTipoArquivo(file.getContentType()); 
				produto.setNomeArquivo(file.getOriginalFilename());

			}
			andView.addObject("produtobj", produtoRepository.save(produto));
			garbageCollection();
			return andView;
		}

		if (produto.getId() != null) {
			Optional<Produto> pdt = produtoRepository.findById(produto.getId());
			if (file.getSize() > 0) {
				produto.setArquivo(file.getBytes());
				produto.setTipoArquivo(file.getContentType());
				produto.setNomeArquivo(file.getOriginalFilename());
			}

			if (pdt.get().getArquivo() != null && file.getSize() == 0) {
				produto.setArquivo(pdt.get().getArquivo());
				produto.setTipoArquivo(pdt.get().getTipoArquivo());
				produto.setNomeArquivo(pdt.get().getNomeArquivo());

			}

			andView.addObject("produtobj", produtoRepository.saveAndFlush(produto));
		}
		garbageCollection();
		return base(andView);
	}

	@GetMapping("baixarArquivoProduto/{idproduto}")
	public void baixarArquivo(@PathVariable("idproduto") Long idproduto,
			HttpServletResponse response) throws IOException {
 
		/* Consultar o obejto pessoa no banco de dados */
		Produto produto = produtoRepository.findById(idproduto).get();
		if (produto.getArquivo() != null) {

			/* Setar tamanho da resposta */
			response.setContentLength(produto.getArquivo().length);

			/*
			 * Tipo do arquivo para download ou pode ser generica application/octet-stream
			 */
			response.setContentType(produto.getTipoArquivo());

			/* Define o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", produto.getNomeArquivo());
			response.setHeader(headerKey, headerValue);

			/* Finaliza a resposta passando o arquivo */
			response.getOutputStream().write(produto.getArquivo());
			garbageCollection();
		}

	}

	@GetMapping("/editarproduto/{idproduto}")
	public ModelAndView editar(@PathVariable("idproduto")  Produto produto) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("produto/cadastroprodutos");
		Pessoa p = new Pessoa();
		if (p.obterUsuarioLogado().equalsIgnoreCase("adm")) {
			andView.addObject("classe", "col-md-6");
		}
		else{
           andView.addObject("classe", "col-md-6 invisible");
		}	
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
		andView.addObject("produtobj", produto);
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Produto"));
		updateVisitas(produto);
		garbageCollection();
		return base(andView);
	}

	@GetMapping("/removerproduto/{idproduto}")
	public String excluir(@PathVariable("idproduto") Long idproduto) {
		try {
			produtoRepository.deleteById(idproduto);
	    } catch (Exception e) {
	     }
	  garbageCollection();
	return "redirect:/listaprodutos";
   }


   public ModelAndView base(ModelAndView modelAndView){
	modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
	modelAndView.addObject("ticket", operacional.getTicket());
	modelAndView.addObject("indicadorGeral", operacional.getIndice());
	modelAndView.addObject("locadoHoje", operacional.getLocado());
    return modelAndView;
  }

  public void grafico() {
	List<listPainelOperacional> grafico = painelRepository.grafico();
	 operacional = grafico.get(0);
	 garbageCollection();
	}

    @GetMapping("/listaprodutos")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("produto/produtos-datatable");
		grafico();
		garbageCollection();
		return base(andView);	 
		} 

   @GetMapping("/serverProdutos")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new ProdutoDataTablesService().execute(produtoRepository, request);
			return ResponseEntity.ok(data);
	}


	
}