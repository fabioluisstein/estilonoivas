package loja.springboot.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.repository.GraficoRepository;
import loja.springboot.repository.GraficoRepository.listGraficoCard;
import loja.springboot.repository.GraficoRepository.listGraficoPrincipal;
import loja.springboot.repository.GraficoRepository.listGraficoSecundario;
import loja.springboot.repository.LocacaoProdutoRepository;
import loja.springboot.repository.LocacaoProdutoRepository.listLocacaoProduto;
import loja.springboot.service.AjusteDataTablesService;

@Controller
public class AjusteController {

	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;

    @Autowired
	private GraficoRepository graficoRepository;
	
	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}


	@RequestMapping(method = RequestMethod.GET, value = "/listaAjustes")
	public ModelAndView produtos() {
		ModelAndView andView = new ModelAndView("produto/ajustes");
		andView.addObject("produtosContrato", locacaoProdutoRepository.locacoesProdutos()); 
		garbageCollection();
		return andView;
	} 
	 /* 
		@GetMapping("/tabelaAjustes")
		public String showTabela( ) {		
			return "produto/ajustes-datatables";
		}
*/
        @Transactional(readOnly = true)
		@GetMapping("/tabelaAjustes")
		public ModelAndView showTabela2( ) {
	     ModelAndView andView = new ModelAndView("produto/ajustes-datatable");
		 List<listGraficoPrincipal> grafico = graficoRepository.graficoPrincipal();
		 List<listGraficoCard> graficoCard = graficoRepository.graficoCard();
		 List<listGraficoSecundario> graficoSecundario = graficoRepository.graficoSecundario();
		 andView.addObject("qtdLocacao", graficoSecundario.get(0).getLocacao());
		 andView.addObject("ticket", grafico.get(0).getTicket());
		 andView.addObject("indicadorGeral", graficoSecundario.get(0).getIndicador());
		 andView.addObject("locadoHoje", graficoCard.get(0).getLocadoHoje());
		  return andView;	
		}
		
		
	
		@GetMapping("/serverAjustes")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new AjusteDataTablesService().execute(locacaoProdutoRepository, request);
			return ResponseEntity.ok(data);
		}
	 
	
		@GetMapping("/editarmodalajuste/{id}")
		public ResponseEntity<?> preEditarAjuste(@PathVariable("id") Long id) {
			listLocacaoProduto  ajuste = locacaoProdutoRepository.findAjusteById(id);	
			return ResponseEntity.ok(ajuste); 
		}
	

	
		@PostMapping("/editarmodalajuste")
		public ResponseEntity<?> editarPromocao(@Valid listLocacaoProduto dto, BindingResult result) {
	
			if (result.hasErrors()) {			
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}			
				return ResponseEntity.unprocessableEntity().body(errors);
			}
			
			@SuppressWarnings("null")
			LocacaoProduto locacaoProduto = locacaoProdutoRepository.findById(dto.getId()).get();
			locacaoProduto.setObservacao(dto.getAjuste());
			
			locacaoProdutoRepository.save(locacaoProduto);
			
			return ResponseEntity.ok().build();
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
		garbageCollection();
		return andView;
	} 
	
	/**
	 * @param idprodutoLocacao
	 * @return
	 */

	@GetMapping("/liberarProdutoAjax/{idprodutoLocacao}")
	public ResponseEntity<?> liberacaoProduto(@PathVariable("idprodutoLocacao") Long idprodutoLocacao)  {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findProdutoLocaoById(idprodutoLocacao).get(0);
		locacaoProduto.setData_liberacao(new Date());
		locacaoProdutoRepository.save(locacaoProduto);
		garbageCollection();
		return ResponseEntity.ok().build();
	} 


	@GetMapping("/liberarProdutoNew/{idproduto}")
	public String liberacaoProdutoNew(@PathVariable("idproduto") Long idproduto)  {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findProdutoLocaoById(idproduto).get(0);
		locacaoProduto.setData_liberacao(new Date());
		locacaoProdutoRepository.save(locacaoProduto);
		garbageCollection();
		return "redirect:/tabelaAjustes";
	} 

}