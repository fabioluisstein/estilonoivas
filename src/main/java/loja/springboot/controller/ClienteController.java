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

	private ModelAndView andViewLista = new ModelAndView("cliente/lista");
	private ModelAndView andViewCadastro = new ModelAndView("cliente/cadastrocliente");
 
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		andViewLista.addObject("clientes", clienteRepository.clientesTodos());
		return andViewLista;
	} 

	@GetMapping("/listaClienteCidade/{idcidade}")
	public ModelAndView clientesCidades(@PathVariable("idcidade") Long idcidade) {
		andViewLista.addObject("clientes", clienteRepository.listaClienteCidade(idcidade));
		return andViewLista;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
		andViewCadastro.addObject("clientebj", new Cliente());
		andViewCadastro.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		return andViewCadastro;
	}
	
	@CacheEvict(value = { "clienteTodosDto","locacoes"}, allEntries = true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
		andViewCadastro.addObject("clientebj",clienteRepository.saveAndFlush(cliente));
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		return andViewCadastro;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Cliente cliente) {
		andViewCadastro.addObject("clientebj",cliente);
		andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		return andViewCadastro;
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