package loja.springboot.controller;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.Visita;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.VisitaRepository;

@Controller
public class VisitaController {
 
	@Autowired
	private VisitaRepository visitaRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}
  
	@RequestMapping(method = RequestMethod.GET, value = "/listavisitas")
	public ModelAndView visitas() {
		ModelAndView andViewLista = new ModelAndView("visita/lista");
		andViewLista.addObject("visitas", visitaRepository.listaTodos());
		garbageCollection();
		return andViewLista; 
	} 

	@RequestMapping(method = RequestMethod.GET, value = "cadastrovisita")
	public ModelAndView cadastro(Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisita");
		visita.setData_inicial(new Date());
		andViewCad.addObject("visitabj", visita);
		andViewCad.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		garbageCollection();
		return andViewCad;
	}


	@CacheEvict(value = { "listVisitas"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarvisita")
	public ModelAndView salvar(Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisita");
		andViewCad.addObject("visitabj",visitaRepository.save(visita));
		andViewCad.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCad;
	}

	

	@GetMapping("/editarvisita/{idvisita}")
	public ModelAndView editar(@PathVariable("idvisita") Visita visita) {
		ModelAndView andViewCad = new ModelAndView("visita/cadastrovisita");
		andViewCad.addObject("visitabj",visita);
		andViewCad.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCad;
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



} 