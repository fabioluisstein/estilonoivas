package loja.springboot.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import loja.springboot.model.Medico;
import loja.springboot.model.Perfil;
import loja.springboot.model.PerfilTipo;
import loja.springboot.model.Usuario;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.UsuarioRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.MedicoService;
import loja.springboot.service.UsuarioService;


@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;

	@Autowired
  private UsuarioRepository usuarioRepository;


	@Autowired
	private MedicoService medicoService;

    private listPainelOperacional operacional;

	@Autowired
	private PainelRepository painelRepository;
	
    // abrir cadastro de usuarios (atendente/admin/cliente)
    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdminParaAdminAtendenteCliente(Usuario usuario) {

        return "usuario/cadastro";
    }
    

	public ModelAndView base(ModelAndView modelAndView){
		modelAndView.addObject("qtdLocacao", operacional.getLocacoes()); 
		modelAndView.addObject("ticket", operacional.getTicket());
		modelAndView.addObject("indicadorGeral", operacional.getIndice());
		modelAndView.addObject("locadoHoje", operacional.getLocado());
		return modelAndView;
	  }

	  public void grafico() {
		List<listPainelOperacional> grafico = painelRepository.grafico();
		operacional = grafico.get(0);
		garbageCollection();
	   }
   

    // abrir lista de usuarios
    @GetMapping("/lista")
    public String listarUsuarios() {
       return "usuario/lista";
    }  

    // listar usuarios na datatables
    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
        return ResponseEntity.ok(service.buscarTodos(request));
    } 
    

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}
    // salvar cadastro de usuarios por administrador
    @PostMapping("/cadastro/salvar")
    public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
    	List<Perfil> perfis = usuario.getPerfis();
    	if (perfis.size() > 2 || 
    			perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
    			perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
    		attr.addFlashAttribute("falha", "Cliente não pode ser Admin e/ou Atendente.");
    		attr.addFlashAttribute("usuario", usuario);
    	} else {
    		try {
    			service.salvarUsuario(usuario); 
    			attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
    		} catch (DataIntegrityViolationException ex) {
    			attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente.");
			}
    	}
    	return "redirect:/u/lista";
    }
    
    // pre edicao de credenciais de usuarios 
    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
        return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
    }    


	@GetMapping("/removerusuario/{idusuario}")
	public String excluir(@PathVariable("idusuario") Long idUsuario) {
		try {
		usuarioRepository.deleteById(idUsuario);
	} catch (Exception e) {
	}
	garbageCollection();
	return "redirect:/u/lista";
  }
      
    // pre edicao de cadastro de usuarios
    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId, 
    												   @PathVariable("perfis") Long[] perfisId) {
    	Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);
    	
   
    		
    		return new ModelAndView("usuario/cadastro", "usuario", us);
    	
    }  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
