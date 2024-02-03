package loja.springboot.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Cliente;
import loja.springboot.repository.CidadeRepository;
import loja.springboot.repository.ClienteRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.ClienteDataTablesService;

@Controller
public class ClienteController {
 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PainelRepository painelRepository;
	
	private listPainelOperacional operacional;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	public void grafico() {
		List<listPainelOperacional> grafico = painelRepository.grafico();
	        operacional = grafico.get(0);
			garbageCollection();
		}

	public ModelAndView base(ModelAndView modelAndView){
		modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
		modelAndView.addObject("ticket", operacional.getTicket());
		modelAndView.addObject("indicadorGeral", operacional.getIndice());
		modelAndView.addObject("locadoHoje", operacional.getLocado());
	  return modelAndView;
	}
		
	@GetMapping("/listaClienteCidade/{idcidade}")
	public ModelAndView clientesCidades(@PathVariable("idcidade") Long idcidade) {
	 ModelAndView andViewLista = new ModelAndView("cliente/lista");
		  andViewLista.addObject("clientes", clienteRepository.listaClienteCidade(idcidade));
		  garbageCollection();
	 return andViewLista;
	}

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView cadastro(Cliente cliente) {
	 ModelAndView andViewCadastro = new ModelAndView("cliente/cadastroclientes");
		  andViewCadastro.addObject("clientebj", new Cliente());
		  andViewCadastro.addObject("cidades", cidadeRepository.cidadeDtoRelac());
		  andViewCadastro.addObject("id", "Cadastrando Cliente");
		  andViewCadastro.addObject("color", "alert alert-dark");
		  garbageCollection();
	 return base(andViewCadastro);
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarcliente")
	public ModelAndView salvar(Cliente cliente) {
	 ModelAndView andViewCadastro = new ModelAndView("cliente/cadastroclientes");
		  andViewCadastro.addObject("clientebj",clienteRepository.save(cliente));
		  andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		  andViewCadastro.addObject("id", "Gravado com Sucesso");
		  andViewCadastro.addObject("color", "alert alert-success");
		  garbageCollection();
	  return base(andViewCadastro);
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Cliente cliente) {
	 ModelAndView andViewCadastro = new ModelAndView("cliente/cadastroclientes");
		  andViewCadastro.addObject("clientebj",cliente);
		  andViewCadastro.addObject("cidades", cidadeRepository.findAll()); 
		  andViewCadastro.addObject("id", "Editando Registro");
		  andViewCadastro.addObject("color", "alert alert-primary");
		  garbageCollection();
	 return base(andViewCadastro);
	}


	@GetMapping("/removercliente/{idcliente}")
    public String excluir(@PathVariable("idcliente") Long idcliente) {
	try {
	   clienteRepository.deleteById(idcliente);	
		 } catch (Exception e) {
		}
	   return "redirect:/listaclientes";
	}

    @GetMapping("/listaclientes")
	public ModelAndView showTabelas() {
	 ModelAndView andView = new ModelAndView("cliente/clientes-datatable");
	 grafico();
	 garbageCollection();
	 return base(andView);	 
	}

   @GetMapping("/serverClientes")
	 public ResponseEntity<?> datatables(HttpServletRequest request) {
	  Map<String, Object> data = new ClienteDataTablesService().execute(clienteRepository, request);
	  return ResponseEntity.ok(data);
   }
} 