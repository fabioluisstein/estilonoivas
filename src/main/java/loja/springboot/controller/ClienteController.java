package loja.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Cliente;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.ClienteDtoRepository;
import loja.springboot.repository.ClienteRepository;

@Controller
public class ClienteController {
 

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteDtoRepository clientedtoRepository;
 
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clientedtoRepository.clientesTodos());
		return andView;
	} 
	 
	@GetMapping("/listaClienteCidade/{idcidade}")
	public ModelAndView clientesCidades(@PathVariable("idcidade") Long idcidade) {
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clientedtoRepository.listaClienteCidade(idcidade));
		return andView;
	}


	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("cliente/cadastrocliente");
		modelAndView.addObject("clientebj", new Cliente());
		modelAndView.addObject("clientes", clienteRepository.findAll());
		modelAndView.addObject("cidades", cidadeRepository.cidadesOrdem());
		return modelAndView;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
		ModelAndView andView = new ModelAndView("cliente/cadastrocliente");
		andView.addObject("cidades", cidadeRepository.cidadesOrdem());
		andView.addObject("clientebj",clienteRepository.saveAndFlush(cliente));
		return andView;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Long idcliente) {
		Optional<Cliente> cliente = clienteRepository.findById(idcliente);
		
		return salvar(cliente.get());
	}
	
	
	@GetMapping("/removercliente/{idcliente}")
	public ModelAndView excluir(@PathVariable("idcliente") Long idcliente) {
		clienteRepository.deleteById(idcliente);	
		ModelAndView andView = new ModelAndView("cliente/lista");
		andView.addObject("clientes", clientedtoRepository.findAll());
		return andView;
	}
	
} 