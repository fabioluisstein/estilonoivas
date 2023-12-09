package loja.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.Cidade;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.EstadoRepository;
import loja.springboot.repository.InterfaceGeneric;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.CidadeService;

@Controller
public class CidadeController {
 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
    private PainelRepository painelRepository;
    private listPainelOperacional operacional;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	@PostMapping("/pesquisarcidade")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cidade/lista");
		modelAndView.addObject("cidades", cidadeRepository.listCidadades());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocidade")
	public ModelAndView cadastro(Cidade cidade) {
		ModelAndView modelAndView = new ModelAndView("cidade/cadastrocidades");
		modelAndView.addObject("id", "Cadastrando Cidade");
		modelAndView.addObject("color", "alert alert-dark");
		modelAndView.addObject("cidadebj", new Cidade());
		modelAndView.addObject("listaEstados", estadoRepository.listEstados());
		garbageCollection();
		return base(modelAndView);
	}
	
	@CacheEvict(value = { "cidadesTodas", "cidadeDtoRelac","forncedoresTodosDto","locacoes120"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcidades")
    public ModelAndView salvar(Cidade cidade) {	
	 ModelAndView andView = new ModelAndView("cidade/cadastrocidades");
	  andView.addObject("cidadebj",cidadeRepository.saveAndFlush(cidade));
	  andView.addObject("listaEstados", estadoRepository.listEstados());
	  andView.addObject("id", "Gravado com Sucesso");
	  andView.addObject("color", "alert alert-success"); 
	  garbageCollection();	
	return base(andView);

	} 
	


	/* 	@RequestMapping(method = RequestMethod.POST, value ="salvarcidades")
	public String salvar2(Cidade cidade) {	
	
		Cidade cdCidade = cidadeRepository.save(cidade);
		garbageCollection();		
		return "redirect:/editarcidade/"+cdCidade.getId().toString();
	} 
	*/


	@GetMapping("employees")
	  public String getEmployees(Pageable pageable, Model model) {
	      Page<Cidade> page = cidadeRepository.findAll(pageable);
	      model.addAttribute("page", page);
	      return "cidade/employee-page";
	  }
	
	@GetMapping("telanova")
	  public String getEEmployees() {
	      return "cidade/lista2";
	  }
 	
	@GetMapping("/editarcidade/{idcidade}")
	public ModelAndView editar(@PathVariable("idcidade") Cidade cidade) {
		ModelAndView andView = new ModelAndView("cidade/cadastrocidades");
		andView.addObject("cidadebj",cidade);
	    andView.addObject("listaEstados", estadoRepository.listEstados());
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
		garbageCollection();
		return base(andView);
	}  



	
    @RequestMapping("/filtro")
    public @ResponseBody
    List<InterfaceGeneric.listGeneric> filtradas(String nome) {
        return estadoRepository.filtradas(nome.toLowerCase());   
    }

	@CacheEvict(value = { "cidadesTodas", "cidadeDtoRelac","forncedoresTodosDto","locacoes120"}, allEntries = true)
	@GetMapping("/removercidade/{idcidade}")
	public String excluir(@PathVariable("idcidade") Long idcidade) {
		try {
		  cidadeRepository.deleteById(idcidade);	
		} catch (Exception e) {
	  }
	  return "redirect:/listacidades";
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
		
	@GetMapping("/listacidades")
	 public ModelAndView showTabela2() {
	  grafico();
	  ModelAndView andView = new ModelAndView("cidade/cidades-datatable");
	  return base(andView);
	}

   @GetMapping("/serverCidades")
	 public ResponseEntity<?> datatables(HttpServletRequest request) {
	  Map<String, Object> data = new CidadeService().execute(cidadeRepository, request);
	return ResponseEntity.ok(data);
   }

	
}