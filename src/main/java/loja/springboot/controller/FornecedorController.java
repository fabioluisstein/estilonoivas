package loja.springboot.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Fornecedor;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.FornecedorDataTablesService;

@Controller
public class FornecedorController {
 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;

    @Autowired
	private PainelRepository painelRepository;

	private listPainelOperacional operacional;
 
	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listafornecedoress")
	public ModelAndView fornecedores() {
		ModelAndView andView = new ModelAndView("fornecedor/lista");
		andView.addObject("fornecedores", fornecedorRepository.fornecedoresTodos());
		garbageCollection();
		return andView;
	}
	 
	@RequestMapping(method = RequestMethod.GET, value = "cadastrofornecedor")
	public ModelAndView cadastro(Fornecedor fornecedor) {
		ModelAndView modelAndView = new ModelAndView("fornecedor/cadastrofornecedores");
		modelAndView.addObject("fornecedorbj", new Fornecedor());
		modelAndView.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		modelAndView.addObject("id", "Cadastrando Fornecedor");
		modelAndView.addObject("color", "alert alert-dark");
		garbageCollection();
		return base(modelAndView);
	}
	
	@CacheEvict(value={"forncedoresTodosDto", "saidas", "saidasRestrito","pagamentosTodos", "listProdutos"} , allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarfornecedor")
	public ModelAndView salvar(Fornecedor fornecedor) {
		ModelAndView andView = new ModelAndView("fornecedor/cadastrofornecedores");
		andView.addObject("cidades", cidadeRepository.findAll());
		andView.addObject("fornecedorbj",fornecedorRepository.saveAndFlush(fornecedor));
		andView.addObject("id", "Gravado com Sucesso");
		andView.addObject("color", "alert alert-success");
		grafico();
		garbageCollection();
		return base(andView);
	}
	
	@GetMapping("/editarfornecedor/{idfornecedor}")
	public ModelAndView editar(@PathVariable("idfornecedor") Fornecedor fornecedor) {
		ModelAndView andView = new ModelAndView("fornecedor/cadastrofornecedores");
		andView.addObject("fornecedorbj",fornecedor);
		andView.addObject("cidades", cidadeRepository.findAll());
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
		grafico();
		garbageCollection();
		return base(andView);
	}
	
	@CacheEvict(value={"forncedoresTodosDto", "saidas", "saidasRestrito", "pagamentosTodos", "listProdutos"} , allEntries=true)
	@GetMapping("/removerfornecedor/{idfornecedor}")
	public String excluir(@PathVariable("idfornecedor") Long idfornecedor) {
		try {
		   fornecedorRepository.deleteById(idfornecedor);	
	    } catch (Exception e) {
	     }
	return "redirect:/listafornecedores";
  } 
	
  
	public void grafico() {
		List<listPainelOperacional> grafico = painelRepository.grafico();
	        operacional = grafico.get(0);
			garbageCollection();
		}

		public ModelAndView base(ModelAndView modelAndView){
			modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
			modelAndView.addObject("ticket", operacional.getTicket());
			modelAndView.addObject("indicadorGeral", operacional.getIndice());
			modelAndView.addObject("locadoHoje", operacional.getLocado());
			return modelAndView;  
		}

  @GetMapping("/listafornecedores")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("fornecedor/fornecedor-datatable");
		grafico();
		garbageCollection();
		return base(andView);	 
		}

   @GetMapping("/serverFornecedores")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new FornecedorDataTablesService().execute(fornecedorRepository, request);
			return ResponseEntity.ok(data);
	} 
	
}