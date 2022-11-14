package loja.springboot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.dto.EntidadeDTO;
import loja.springboot.model.Cidade;
import loja.springboot.model.Estado;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.EstadoRepository;

@Controller
public class CidadeController {
 

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	
 
	@Cacheable("cidades") 
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
		return modelAndView;
	}
	
	@CacheEvict(value="cidades",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcidade")
	public String salvar(Cidade cidade, BindingResult result, @RequestParam(name = "estadoId", required = false) Long estadoId) {	

		cidade.setEstado(new Estado(estadoId));
		Cidade cdCidade = cidadeRepository.saveAndFlush(cidade);		
		return "redirect:/editarcidade/"+cdCidade.getId().toString();
		
	} 
	
 	
	@GetMapping("/editarcidade/{idcidade}")
	public ModelAndView editar(@PathVariable("idcidade") Long idcidade) {
		Optional<Cidade> cidade = cidadeRepository.findById(idcidade);
		ModelAndView andView = new ModelAndView("cidade/cadastrocidade");
		andView.addObject("cidadebj",cidade);
		andView.addObject("estadodto", cidade.get().getEstado().getNome());
		andView.addObject("estadoId", cidade.get().getEstado().getId());
		
		  
		return andView;
	}  
	
    @RequestMapping("/filtro")
    public @ResponseBody
    List<EntidadeDTO> filtradas(String nome) {
        return estadoRepository.filtradas(nome.toLowerCase());   
    }
    
	@CacheEvict(value="cidades",allEntries=true)
	@GetMapping("/removercidade/{idcidade}")
	public ModelAndView excluir(@PathVariable("idcidade") Long idcidade) {
		cidadeRepository.deleteById(idcidade);	
		ModelAndView andView = new ModelAndView("cidade/lista");
		andView.addObject("cidades", cidadeRepository.top10());
		return andView;
	}
	
}