package loja.springboot.controller;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import loja.springboot.model.Categorias;
import loja.springboot.model.Promocao;
import loja.springboot.repository.CategoriasRepository;
import loja.springboot.repository.ClienteRepository;
import loja.springboot.repository.ProdutoRepository;
import loja.springboot.repository.PromocaoRepository;
import loja.springboot.service.ClienteDataTablesService;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {
	
	private static Logger log = LoggerFactory.getLogger(PromocaoController.class);
	
    @Autowired
    private PromocaoRepository promocaoRepository; 

	@Autowired
    private ClienteRepository clienteRepository;
	@Autowired 
	private CategoriasRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;
	

// ======================================ADD LIKES===============================================
	
@PostMapping("/like/{id}")
public ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id) {
	promocaoRepository.updateSomarLikes(id);
	int likes = promocaoRepository.findLikesById(id);
	return ResponseEntity.ok(likes);
}



	// ======================================AUTOCOMPLETE===============================================
	
	@GetMapping("/site")
	public ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo) {
		List<String> sites = promocaoRepository.findSitesByTermo(termo);
		return ResponseEntity.ok(sites);
	}

	@GetMapping("/site/list")
	public String listarPorSite(@RequestParam("site") Long id, ModelMap model) {
		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("promocoes", promocaoRepository.findBySite(id, pageRequest));
		return "promo-card";
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> salvarPromocao(@Valid Promocao promocao, BindingResult result) {
		
		if (result.hasErrors()) {
			
			Map<String, String> errors = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		
		log.info("Promocao {}", promocao.toString());
		promocao.setDtCadastro(LocalDateTime.now());
		promocaoRepository.save(promocao);
		return ResponseEntity.ok().build();
	}
	



	@GetMapping("/list")
	public String listarOfertas(ModelMap model) {

  
		// Sort sort = new Sort(Sort.Direction.DESC, "dtCadastro");
		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("promocoes", produtoRepository.findAll(pageRequest));
		return "promo-list";
	}
	 
	@GetMapping("/list/ajax")
	public String listarCards(@RequestParam(name = "page", defaultValue = "1") int page, ModelMap model) {
		
		PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.ASC, "id"));

		model.addAttribute("promocoes", produtoRepository.findAll(pageRequest));		
		return "promo-card";
	}	


	@GetMapping("/tabela")
	public String showTabela( ) {
		return "promo-datatables";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> datatables(HttpServletRequest request) {
		Map<String, Object> data = new ClienteDataTablesService().execute(clienteRepository, request);
		return ResponseEntity.ok(data);
	}
 
	@ModelAttribute("categorias")
	public List<Categorias> getCategorias() {
		
		return categoriaRepository.findAll(); 
	}

	@GetMapping("/add")
	public String abrirCadastro() {
		
		return "promo-add";
	}
}
