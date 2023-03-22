package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Estado;
import loja.springboot.repository.EstadoRepository;

@Controller
public class EstadoController {
 

	@Autowired
	private EstadoRepository estadoRepository;
 
	@RequestMapping(method = RequestMethod.GET, value = "/listaestados")
	public ModelAndView estados() {
		ModelAndView andView = new ModelAndView("estado/lista");
		andView.addObject("estados", estadoRepository.listEstados());
		return andView;
	}
	 
	
	@PostMapping("/pesquisarestado")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("estado/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("estados", estadoRepository.listEstados());
		}
		modelAndView.addObject("estados", estadoRepository.findEstadoByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastroestado")
	public ModelAndView cadastro(Estado estado) {
		ModelAndView modelAndView = new ModelAndView("estado/cadastroestado");
		modelAndView.addObject("estadobj", new Estado());
		return modelAndView;
	}
	
	
	@CacheEvict(value = { "estadosTodos", "estadoDtoFilter" ,"cidadesTodas"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarestado")
	public ModelAndView salvar(Estado estado) { 
		ModelAndView andView = new ModelAndView("estado/cadastroestado");
		andView.addObject("estadobj",estadoRepository.saveAndFlush(estado));
		return andView;
	}
	
	@GetMapping("/editarestado/{idestado}")
	public ModelAndView editar(@PathVariable("idestado") Long idestado) {
		Optional<Estado> estado = estadoRepository.findById(idestado);
		return salvar(estado.get());
	}
	
	@CacheEvict(value = { "estadosTodos", "estadoDtoFilter" ,"cidadesTodas"}, allEntries = true)
	@GetMapping("/removerestado/{idestado}")
	public String excluir(@PathVariable("idestado") Long idestado) {
		try {
			estadoRepository.deleteById(idestado);	
		} catch (Exception e) {
		}
		return "redirect:/listaestados";
	}
	
} 