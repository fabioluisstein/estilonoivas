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

	private ModelAndView andViewLista = new ModelAndView("visita/lista");
	private ModelAndView andViewCadastro = new ModelAndView("visita/cadastroVisita");
 
	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}
  
	@RequestMapping(method = RequestMethod.GET, value = "/listavisitas")
	public ModelAndView visitas() {
		andViewLista.addObject("visitas", visitaRepository.listaTodos());
		garbageCollection();
		return andViewLista; 
	} 

	@RequestMapping(method = RequestMethod.GET, value = "cadastrovisita")
	public ModelAndView cadastro(Visita visita) {
		visita.setData_inicial(new Date());
		andViewCadastro.addObject("visitabj", visita);
		andViewCadastro.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		garbageCollection();
		return andViewCadastro;
	}


	@CacheEvict(value = { "listVisitas"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarvisita")
	public ModelAndView salvar(Visita visita) {
		andViewCadastro.addObject("visitabj",visitaRepository.save(visita));
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCadastro;
	}

	

	@GetMapping("/editarvisita/{idvisita}")
	public ModelAndView editar(@PathVariable("idvisita") Visita visita) {
		andViewCadastro.addObject("visitabj",visita);
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCadastro;
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


	



/*
	@GetMapping("/listaClienteCidade/{idcidade}")
	public ModelAndView clientesCidades(@PathVariable("idcidade") Long idcidade) {
		andViewLista.addObject("clientes", clienteRepository.listaClienteCidade(idcidade));
		garbageCollection();
		return andViewLista;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
		andViewCadastro.addObject("clientebj", new Cliente());
		andViewCadastro.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		garbageCollection();
		return andViewCadastro;
	}
	
	@CacheEvict(value = { "clienteTodosDto","locacoes120"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
		andViewCadastro.addObject("clientebj",clienteRepository.save(cliente));
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCadastro;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Cliente cliente) {
		andViewCadastro.addObject("clientebj",cliente);
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		garbageCollection();
		return andViewCadastro;
	}


	@CacheEvict(value = { "clienteTodosDto","locacoes120"}, allEntries = true)
	@GetMapping("/removercliente/{idcliente}")
	public String excluir(@PathVariable("idcliente") Long idcliente) {
		try {
			clienteRepository.deleteById(idcliente);	
		  } catch (Exception e) {
		}
		return "redirect:/listaclientes";
	  }

	   */

} 