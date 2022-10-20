package loja.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.repository.EstadoRepository;

@Controller
public class EstadoController {
 

	@Autowired
	private EstadoRepository estadoRepository;
 

	@RequestMapping(method = RequestMethod.GET, value = "/listaestados")
	public ModelAndView estados() {
		ModelAndView andView = new ModelAndView("estado/lista");
		andView.addObject("estados", estadoRepository.findAll());
		return andView;
	}
	 
	
	@PostMapping("/pesquisarestado")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("estado/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("estados", estadoRepository.top10());
		}
		modelAndView.addObject("estados", estadoRepository.findEstadoByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	
	
	@GetMapping("/editarestado/{idestado}")
	public ModelAndView editar(@PathVariable("idestado") Long idestado) {
		
		//	Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		//	modelAndView.addObject("pessoas", pessoaRepository.findAll());
	//	modelAndView.addObject("pessoaobj", pessoa.get());
		return modelAndView;
		
	}
	
	
	@GetMapping("/removerestado/{idestado}")
	public ModelAndView excluir(@PathVariable("idestado") Long idestado) {
		
		//pessoaRepository.deleteById(idpessoa);	
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
	//	modelAndView.addObject("pessoas", pessoaRepository.findAll());
	//	modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
		
	}
	
	
	
	
}