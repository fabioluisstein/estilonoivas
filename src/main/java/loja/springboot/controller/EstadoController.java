package loja.springboot.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import loja.springboot.model.Estado;
import loja.springboot.repository.EstadoRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.EstadoService;

@Controller
public class EstadoController {

  @Autowired
  private EstadoRepository estadoRepository;

  @Autowired
  private PainelRepository painelRepository;
  private listPainelOperacional operacional;
 	 
	@RequestMapping(method = RequestMethod.GET, value = "cadastroestado")
	public ModelAndView cadastro(Estado estado) {
		ModelAndView modelAndView = new ModelAndView("estado/cadastroestados");
		modelAndView.addObject("id", "Cadastrando Estado");
		modelAndView.addObject("color", "alert alert-dark");
		modelAndView.addObject("estadobj", new Estado()); 
		 return base(modelAndView);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarestado")
	public ModelAndView salvar(Estado estado) { 
		ModelAndView andView = new ModelAndView("estado/cadastroestados");
		andView.addObject("estadobj",estadoRepository.saveAndFlush(estado));
		andView.addObject("id", "Gravado com Sucesso");
		andView.addObject("color", "alert alert-success");
		 return base(andView);
	}
	
	
	@GetMapping("/editarestado/{idestado}")
	public ModelAndView editar(@PathVariable("idestado") Estado idestado) {
		ModelAndView andView = new ModelAndView("estado/cadastroestados");
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
		andView.addObject("estadobj",estadoRepository.saveAndFlush(idestado));
		 return base(andView);
	}

	
	@GetMapping("/removerestado/{idestado}")
	public String excluir(@PathVariable("idestado") Long idestado) {
		try {
			estadoRepository.deleteById(idestado);
		} catch (Exception e) {
		}
	  return "redirect:/listaestados";
	}

	public ModelAndView base(ModelAndView modelAndView){
		modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
		modelAndView.addObject("ticket", operacional.getTicket());
		modelAndView.addObject("indicadorGeral", operacional.getIndice());
		modelAndView.addObject("locadoHoje", operacional.getLocado());
	   return modelAndView;
	  }
	  
	public void garbageCollection() {
	  Runtime.getRuntime().gc();
	  Runtime.getRuntime().freeMemory();
	}
 
	
	public void grafico() {
	List<listPainelOperacional> grafico = painelRepository.grafico();
	 operacional = grafico.get(0);
	 garbageCollection();
	}
		
	@GetMapping("/listaestados")
	 public ModelAndView showTabela2() {
	  grafico();
	  ModelAndView andView = new ModelAndView("estado/estados-datatable");
	  return base(andView);
	}

   
   @GetMapping("/serverEstados")
	 public ResponseEntity<?> datatables(HttpServletRequest request) {
	  Map<String, Object> data = new EstadoService().execute(estadoRepository, request);
	return ResponseEntity.ok(data);
   }
	
} 