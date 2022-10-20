package loja.springboot.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Locacao;
import loja.springboot.model.Pagamento;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.ClienteRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.LocacaoRepository;
import loja.springboot.repository.PagamentoRepository;
import loja.springboot.repository.PessoaRepository;

@Controller
public class LocacaoController {
 

	@Autowired
	private PessoaRepository colaboradorRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private LocacaoRepository locacaoRepository;
 
	@Cacheable("locacoes") 
	@RequestMapping(method = RequestMethod.GET, value = "/listalocacoes")
	public ModelAndView locacoes() {
		ModelAndView andView = new ModelAndView("locacao/lista");
		andView.addObject("locacoes", locacaoRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarlocacao")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, 
			                      @RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal)  {
	  
	  ModelAndView modelAndView = new ModelAndView("locacao/lista");
		if(nomepesquisa.isEmpty() && dataInicio.isEmpty() && dataFinal.isEmpty()) {
			modelAndView.addObject("locacoes", locacaoRepository.top10());
		}
		
		if(!dataInicio.isEmpty() && !dataFinal.isEmpty()) {	
			modelAndView.addObject("locacoes", locacaoRepository.findLocacaoDatas(dataInicio,dataFinal));
			return modelAndView;
		}
		
		modelAndView.addObject("locacoes", locacaoRepository.findLocacaoByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}
	
	@Cacheable("locacoes")  
	@RequestMapping(method = RequestMethod.GET, value = "cadastrolocacao")
	public ModelAndView cadastro(Pagamento locacao) {
		ModelAndView modelAndView = new ModelAndView("locacao/cadastrolocacao");
		modelAndView.addObject("locacaobj", new Pagamento());
		modelAndView.addObject("colaboradores", colaboradorRepository.findAll());
		modelAndView.addObject("clientes", clienteRepository.findAll());
		return modelAndView;
	}
	
	@CacheEvict(value="locacoes",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarlocacao",consumes= {"multipart/form-data"})
	public ModelAndView salvar(Locacao locacao, final MultipartFile file) throws IOException {	
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("clientes", clienteRepository.findAll());
		andView.addObject("locacaobj", locacaoRepository.saveAndFlush(locacao));
	  return andView;
	} 
	
	

	@GetMapping("/editarlocacao/{idlocacao}")
	public ModelAndView editar(@PathVariable("idlocacao") Long idlocacao) throws ParseException, IOException {
		Optional<Locacao> locacao = locacaoRepository.findById(idlocacao);
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("locacaobj",locacao);
		andView.addObject("colaboradores", colaboradorRepository.findAll());
		andView.addObject("clientes", clienteRepository.findAll());
		
		return andView;
	}
	
	
	@GetMapping("/removerlocacao/{idlocacao}")
	public ModelAndView excluir(@PathVariable("idlocacao") Long idlocacao) {
		locacaoRepository.deleteById(idlocacao);	
		ModelAndView andView = new ModelAndView("locacao/lista");
		andView.addObject("locacoes", locacaoRepository.top10());
		return andView;
	}
	
}