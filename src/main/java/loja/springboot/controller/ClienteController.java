package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 
	@Cacheable("clientes") 
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clienteRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarcliente")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cliente/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("clientes", clienteRepository.top10());
		}
		modelAndView.addObject("clientes", clienteRepository.findClienteByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@Cacheable("clientes") 
	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("cliente/cadastrocliente");
		modelAndView.addObject("clientebj", new Cliente());
		modelAndView.addObject("clientes", clienteRepository.findAll());
		modelAndView.addObject("cidades", cidadeRepository.findAll());
		return modelAndView;
	}
	
	@CacheEvict(value="clientes",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
		ModelAndView andView = new ModelAndView("cliente/cadastrocliente");
		andView.addObject("cidades", cidadeRepository.findAll());
		andView.addObject("clientebj",clienteRepository.saveAndFlush(cliente));
		return andView;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Long idcliente) {
		Optional<Cliente> cliente = clienteRepository.findById(idcliente);
		
		return salvar(cliente.get());
	}
	
	@CacheEvict(value="clientes",allEntries=true)
	@GetMapping("/removercliente/{idcliente}")
	public ModelAndView excluir(@PathVariable("idcliente") Long idcliente) {
		clienteRepository.deleteById(idcliente);	
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clienteRepository.top10());
		return andView;
	}
	
}