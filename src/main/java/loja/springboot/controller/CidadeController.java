package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Cidade;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.EstadoRepository;

@Controller
public class CidadeController {
 

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
 

	@RequestMapping(method = RequestMethod.GET, value = "/listacidades")
	public ModelAndView cidades() {
		ModelAndView andView = new ModelAndView("cidade/lista");
		andView.addObject("cidades", cidadeRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarcidade")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cidade/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("cidades", cidadeRepository.top10());
		}
		modelAndView.addObject("cidades", cidadeRepository.findCidadeByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocidade")
	public ModelAndView cadastro(Cidade cidade) {
		ModelAndView modelAndView = new ModelAndView("cidade/cadastrocidade");
		modelAndView.addObject("cidadebj", new Cidade());
		modelAndView.addObject("estados", estadoRepository.findAll());
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarcidade")
	public ModelAndView salvar(Cidade cidade) {
		ModelAndView andView = new ModelAndView("cidade/cadastrocidade");
		andView.addObject("estados", estadoRepository.findAll());
		andView.addObject("cidadebj",cidadeRepository.saveAndFlush(cidade));
		return andView;
	}
	
	@GetMapping("/editarcidade/{idcidade}")
	public ModelAndView editar(@PathVariable("idcidade") Long idcidade) {
		Optional<Cidade> cidade = cidadeRepository.findById(idcidade);
		
		return salvar(cidade.get());
	}
	
	@GetMapping("/removercidade/{idcidade}")
	public ModelAndView excluir(@PathVariable("idcidade") Long idcidade) {
		cidadeRepository.deleteById(idcidade);	
		ModelAndView andView = new ModelAndView("cidade/lista");
		andView.addObject("cidades", cidadeRepository.top10());
		return andView;
	}
	
}