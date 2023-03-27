package loja.springboot.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.Cidade;
import loja.springboot.model.Estado;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.EstadoRepository;
import loja.springboot.repository.InterfaceGeneric;

@Controller
public class CidadeController {
 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/listacidades")
	public ModelAndView cidades() {
		ModelAndView andView = new ModelAndView("cidade/lista");
		andView.addObject("cidades", cidadeRepository.listCidadades());
		return andView;
	}
	 
	@PostMapping("/pesquisarcidade")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cidade/lista");
		modelAndView.addObject("cidades", cidadeRepository.listCidadades());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocidade")
	public ModelAndView cadastro(Cidade cidade) {
		ModelAndView modelAndView = new ModelAndView("cidade/cadastrocidade");
		modelAndView.addObject("cidadebj", new Cidade());
		return modelAndView;
	}
	
	@CacheEvict(value = { "cidadesTodas", "cidadesOrdem" ,"cidadeDtoRelac","clienteTodosDto","forncedoresTodosDto"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcidade")
	public String salvar(Cidade cidade, BindingResult result, @RequestParam(name = "estadoId", required = false) Long estadoId) {	
		cidade.setEstado(new Estado(estadoId));
		Cidade cdCidade = cidadeRepository.saveAndFlush(cidade);		
		return "redirect:/editarcidade/"+cdCidade.getId().toString();
	} 
	
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
		ModelAndView andView = new ModelAndView("cidade/cadastrocidade");
		andView.addObject("cidadebj",cidade);
		andView.addObject("estadodto", cidade.getEstado().getNome());
		andView.addObject("estadoId", cidade.getEstado().getId());
		return andView;
	}  
	
    @RequestMapping("/filtro")
    public @ResponseBody
    List<InterfaceGeneric.listGeneric> filtradas(String nome) {
        return estadoRepository.filtradas(nome.toLowerCase());   
    }

	@CacheEvict(value = { "cidadesTodas", "cidadesOrdem" ,"cidadeDtoRelac","clienteTodosDto","forncedoresTodosDto"}, allEntries = true)
	@GetMapping("/removercidade/{idcidade}")
	public String excluir(@PathVariable("idcidade") Long idcidade) {
		try {
		  cidadeRepository.deleteById(idcidade);	
		} catch (Exception e) {
	  }
	  return "redirect:/listacidades";
	}
	
}