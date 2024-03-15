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
import loja.springboot.model.Pagamento;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.PagamentoRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.PagamentoDataTablesService;

@Controller
public class PagamentoController {

	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	
	private listPainelOperacional operacional;

    @Autowired
	private PainelRepository painelRepository;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
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

 
	@PostMapping("/pesquisarpagamento")
	public ModelAndView pesquisar(@RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal) {
		ModelAndView modelAndView = new ModelAndView("pagamento/lista");
		if (dataInicio.isEmpty() && dataFinal.isEmpty()) {
			modelAndView.addObject("pagamentos", pagamentoRepository.findAllPagamentosTodos());
		}
		if (!dataInicio.isEmpty() && !dataFinal.isEmpty()) {
			modelAndView.addObject("pagamentos", pagamentoRepository.findPagamentoDatas(dataInicio, dataFinal));
			return modelAndView;
		}
		modelAndView.addObject("pagamentos", pagamentoRepository.findAllPagamentosTodos());
		garbageCollection();
		return modelAndView;
	}


	@RequestMapping(method = RequestMethod.GET, value = "cadastropagamento")
	public ModelAndView cadastro(Pagamento pagamento) {
		ModelAndView modelAndView = new ModelAndView("pagamento/cadastropagamentos");
		modelAndView.addObject("pagamentobj", new Pagamento());
		modelAndView.addObject("fornecedores", fornecedorRepository.fornecedoresTodos());
		modelAndView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		modelAndView.addObject("id", "Cadastrando Pagamento");
		modelAndView.addObject("color", "alert alert-dark");
		garbageCollection();
		return base(modelAndView);	 
	}

	@RequestMapping(method = RequestMethod.POST, value = "salvarpagamento", consumes = { "multipart/form-data" })
	public ModelAndView salvar(Pagamento pagamento, final MultipartFile file) throws IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamentos");
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		andView.addObject("id", "Gravado com Sucesso");
		andView.addObject("color", "alert alert-success");
		if (pagamento.getId() == null) {
			if (file.getSize() > 0) {
				pagamento.setArquivo(file.getBytes());
				pagamento.setTipoArquivo(file.getContentType());
				pagamento.setNomeArquivo(file.getOriginalFilename());

			}
			andView.addObject("pagamentobj", pagamentoRepository.save(pagamento));
			garbageCollection();
			base(andView);	
		}

		if (pagamento.getId() != null) {
			Optional<Pagamento> pdt = pagamentoRepository.findById(pagamento.getId());
			if (file.getSize() > 0) {
				pagamento.setArquivo(file.getBytes());
				pagamento.setTipoArquivo(file.getContentType());
				pagamento.setNomeArquivo(file.getOriginalFilename());
			}

			if (pdt.get().getArquivo() != null && file.getSize() == 0) {
				pagamento.setArquivo(pdt.get().getArquivo());
				pagamento.setTipoArquivo(pdt.get().getTipoArquivo());
				pagamento.setNomeArquivo(pdt.get().getNomeArquivo());

			}
			andView.addObject("pagamentobj", pagamentoRepository.saveAndFlush(pagamento));
			
		}
		garbageCollection();
		return base(andView);	 
	}

	@GetMapping("/baixarArquivoPagamento/{idpagamento}")
	public void baixarArquivo(@PathVariable("idpagamento") Long idpagamento,
			HttpServletResponse response) throws IOException {

		/* Consultar o obejto pessoa no banco de dados */
		Pagamento pagamento = pagamentoRepository.findById(idpagamento).get();
		if (pagamento.getArquivo() != null) {

			/* Setar tamanho da resposta */
			response.setContentLength(pagamento.getArquivo().length);

			/*
			 * Tipo do arquivo para download ou pode ser generica application/octet-stream
			 */
			response.setContentType(pagamento.getTipoArquivo());

			/* Define o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pagamento.getNomeArquivo());
			response.setHeader(headerKey, headerValue);

			/* Finaliza a resposta passando o arquivo */
			response.getOutputStream().write(pagamento.getArquivo());
			garbageCollection();
		}

	}

	@GetMapping("/editarpagamento/{idpagamento}")
	public ModelAndView editar(@PathVariable("idpagamento") Pagamento pagamento) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamentos");
		grafico();
		andView.addObject("pagamentobj", pagamento);
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
	
		garbageCollection();
		return base(andView);
	}

	@GetMapping("/removerpagamento/{idpagamento}")
	public String excluir(@PathVariable("idpagamento") Long idpagamento) {
		try {
		pagamentoRepository.deleteById(idpagamento);
	} catch (Exception e) {
	}
	garbageCollection();
	return "redirect:/listapagamentos";
  }


    @GetMapping("/listapagamentos")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("pagamento/pagamentos-datatables");
		grafico();
		garbageCollection();
		return base(andView);	 
		} 

   @GetMapping("/serverPagamentos")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new PagamentoDataTablesService().execute(pagamentoRepository, request);
			return ResponseEntity.ok(data);   
	}





}