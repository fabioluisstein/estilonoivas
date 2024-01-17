package loja.springboot.controller;
import java.util.Date;
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
import loja.springboot.model.Visita;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.repository.VisitaRepository;
import loja.springboot.service.VisitaDataTablesService;

@Controller
public class VisitaController {
 
	@Autowired
	private VisitaRepository visitaRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	 @Autowired
	private PainelRepository painelRepository;

	private listPainelOperacional operacional;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}
  


	@RequestMapping(method = RequestMethod.GET, value = "cadastrovisita")
	public ModelAndView cadastro(Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisitas");
		visita.setData_inicial(new Date());
		andViewCad.addObject("visitabj", visita);
		andViewCad.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		andViewCad.addObject("id", "Cadastrando Pagamento");
		andViewCad.addObject("color", "alert alert-dark");
		garbageCollection();
		return base(andViewCad);	
	}


	@CacheEvict(value = { "listVisitas"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarvisita")
	public ModelAndView salvar(Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisitas");
		andViewCad.addObject("visitabj",visitaRepository.save(visita));
		andViewCad.addObject("cidades", cidadeRepository.findAll()); 
		andViewCad.addObject("id", "Gravado com Sucesso");
		andViewCad.addObject("color", "alert alert-success");
		garbageCollection();
		return base(andViewCad);	
	}

	

	@GetMapping("/editarvisita/{idvisita}")
	public ModelAndView editar(@PathVariable("idvisita") Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisitas");
		grafico();
		andViewCad.addObject("visitabj",visita);
		andViewCad.addObject("cidades", cidadeRepository.findAll()); 
		andViewCad.addObject("id", "Editando Registro");
		andViewCad.addObject("color", "alert alert-primary");
		garbageCollection();
		return base(andViewCad);
	}

	public ModelAndView base(ModelAndView modelAndView){
		modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
		modelAndView.addObject("ticket", operacional.getTicket());
		modelAndView.addObject("indicadorGeral", operacional.getIndice());
		modelAndView.addObject("locadoHoje", operacional.getLocado());
		return modelAndView;
	  }


	@CacheEvict(value = { "listVisitas"}, allEntries = true)
	@GetMapping("/removervisita/{idvisita}")
	public String excluir(@PathVariable("idvisita") Long idvisita) {
		try {
			visitaRepository.deleteById(idvisita);	
		  } catch (Exception e) {
		}
		return "redirect:/listavisitas";
	  }

	public void grafico() {
	 List<listPainelOperacional> grafico = painelRepository.grafico();
	 operacional = grafico.get(0);
	 garbageCollection();
	}


    @GetMapping("/listavisitas")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("visita/visitas-datatables");
		grafico();
		garbageCollection();
		return base(andView);	 
		} 

   @GetMapping("/serverVisitas")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new VisitaDataTablesService().execute(visitaRepository, request);
			return ResponseEntity.ok(data);   
	}




} 