package loja.springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import loja.springboot.model.Cliente;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.ClienteRepository;

@Controller
public class ClienteController {
 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
 
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clienteRepository.clientesTodos());
		return andView;
	} 
	 
	@GetMapping("/listaClienteCidade/{idcidade}")
	public ModelAndView clientesCidades(@PathVariable("idcidade") Long idcidade) {
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clienteRepository.listaClienteCidade(idcidade));
		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("cliente/cadastrocliente");
		modelAndView.addObject("clientebj", new Cliente());
		modelAndView.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		return modelAndView;
	}
	
	@CacheEvict(value = { "clienteTodosDto","locacoes"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
		ModelAndView andView = new ModelAndView("cliente/cadastrocliente");
		andView.addObject("clientebj",clienteRepository.saveAndFlush(cliente));
		andView.addObject("cidades", cidadeRepository.findAll()); 
		return andView;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Cliente cliente) {
		ModelAndView andView = new ModelAndView("cliente/cadastrocliente");
		andView.addObject("clientebj",cliente);
		andView.addObject("cidades", cidadeRepository.findAll()); 
		return andView;
	}


	@CacheEvict(value = { "clienteTodosDto","locacoes"}, allEntries = true)
	@GetMapping("/removercliente/{idcliente}")
	public String excluir(@PathVariable("idcliente") Long idcliente) {
		try {
			clienteRepository.deleteById(idcliente);	
		  } catch (Exception e) {
		}
		return "redirect:/listaclientes";
	  }

} 