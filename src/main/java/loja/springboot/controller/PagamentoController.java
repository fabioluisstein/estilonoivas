package loja.springboot.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.Pagamento;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.PagamentoRepository;

@Controller
public class PagamentoController {

	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/listapagamentos")
	public ModelAndView pagamentos() {
		ModelAndView andView = new ModelAndView("pagamento/lista");
		andView.addObject("pagamentos", pagamentoRepository.saidasTodos());
		return andView;
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
		return modelAndView;
	}


	@RequestMapping(method = RequestMethod.GET, value = "cadastropagamento")
	public ModelAndView cadastro(Pagamento pagamento) {

		ModelAndView modelAndView = new ModelAndView("pagamento/cadastropagamento");
		modelAndView.addObject("pagamentobj", new Pagamento());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		modelAndView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		return modelAndView;
	}

	@CacheEvict(value = { "saidas", "pagamentosTodos" }, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value = "salvarpagamento", consumes = { "multipart/form-data" })
	public ModelAndView salvar(Pagamento pagamento, final MultipartFile file) throws IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamento");
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		if (pagamento.getId() == null) {
			if (file.getSize() > 0) {
				pagamento.setArquivo(file.getBytes());
				pagamento.setTipoArquivo(file.getContentType());
				pagamento.setNomeArquivo(file.getOriginalFilename());

			}
			andView.addObject("pagamentobj", pagamentoRepository.saveAndFlush(pagamento));
			return andView;
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
		return andView;
	}

	public boolean verificaImagem(MultipartFile file, byte[] imagem) {
		if (file != null && file.getSize() > 0) {

		}
		return true;
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
		}

	}

	@GetMapping("/editarpagamento/{idpagamento}")
	public ModelAndView editar(@PathVariable("idpagamento") Pagamento pagamento) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamento");
		andView.addObject("pagamentobj", pagamento);
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		return andView;
	}

	@CacheEvict(value = { "saidas", "pagamentosTodos" }, allEntries = true)
	@GetMapping("/removerpagamento/{idpagamento}")
	public String excluir(@PathVariable("idpagamento") Long idpagamento) {
		try {
		pagamentoRepository.deleteById(idpagamento);
	} catch (Exception e) {
	}
	return "redirect:/listapagamentos";
  }

}