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

import loja.springboot.model.Produto;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.ProdutoRepository;



@Controller
public class ProdutoController {
 

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
 
	@Cacheable("produtos") 
	@RequestMapping(method = RequestMethod.GET, value = "/listaprodutos")
	public ModelAndView produtos() {
		ModelAndView andView = new ModelAndView("produto/lista");
		andView.addObject("produtos", produtoRepository.top10());
		return andView;
	}
	 
	@PostMapping("/pesquisarproduto")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("produto/lista");
		if(nomepesquisa.isEmpty()) {
			modelAndView.addObject("produtos", produtoRepository.top10());
		}
		modelAndView.addObject("produtos", produtoRepository.findProdutoByName(nomepesquisa.toUpperCase()));
		return modelAndView;
	}

	@Cacheable("produtos")  
	@RequestMapping(method = RequestMethod.GET, value = "cadastroproduto")
	public ModelAndView cadastro(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produto/cadastroproduto");
		modelAndView.addObject("produtobj", new Produto());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		modelAndView.addObject("categorias", categoriaRepository.findCategoriaByTable("Produto"));
		return modelAndView;
	}
	
	@CacheEvict(value="produtos",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value ="salvarproduto",consumes= {"multipart/form-data"})
	public ModelAndView salvar(Produto produto, final MultipartFile file) throws IOException {	
		ModelAndView andView = new ModelAndView("produto/cadastroproduto");
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByTable("Produto"));
		
		if(produto.getId()==null) {
			if(file.getSize()>0) {
				produto.setArquivo(file.getBytes());	  
				produto.setTipoArquivo(file.getContentType());	
				produto.setNomeArquivo(file.getOriginalFilename());	  
				
		    }  
			andView.addObject("produtobj",produtoRepository.saveAndFlush(produto));
			return andView;
		}
		
		if (produto.getId() != null) {
			Optional<Produto> pdt = produtoRepository.findById(produto.getId());
			if (file.getSize() > 0) {
				produto.setArquivo(file.getBytes());
				produto.setTipoArquivo(file.getContentType());	
				produto.setNomeArquivo(file.getOriginalFilename());	 
			}

			if (pdt.get().getArquivo() != null && file.getSize()==0) {
				produto.setArquivo(pdt.get().getArquivo());
				produto.setTipoArquivo(pdt.get().getTipoArquivo());	
				produto.setNomeArquivo(pdt.get().getNomeArquivo());	 
				
			}

			andView.addObject("produtobj", produtoRepository.saveAndFlush(produto));
		}
	  return andView;
	} 
	
	
	
	public boolean verificaImagem(MultipartFile file, byte[] imagem) {
		if(file!=null && file.getSize()>0) {
			
		}
		return true;
	
	}
	

	@GetMapping("/baixarArquivo/{idproduto}")
	public void baixarArquivo(@PathVariable("idproduto") Long idproduto, 
			HttpServletResponse response) throws IOException {
		
		/*Consultar o obejto pessoa no banco de dados*/
		Produto produto = produtoRepository.findById(idproduto).get();
		if (produto.getArquivo() != null) {
	
			/*Setar tamanho da resposta*/
			response.setContentLength(produto.getArquivo().length);
			
			/*Tipo do arquivo para download ou pode ser generica application/octet-stream*/
			response.setContentType(produto.getTipoArquivo());
			
			/*Define o cabeçalho da resposta*/
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", produto.getNomeArquivo());
			response.setHeader(headerKey, headerValue);
			
			/*Finaliza a resposta passando o arquivo*/
			response.getOutputStream().write(produto.getArquivo());
			
		}
		
	} 
	
	
	
 
	@GetMapping("/editarproduto/{idproduto}")
	public ModelAndView editar(@PathVariable("idproduto") Long idproduto) throws ParseException, IOException {
		Optional<Produto> produto = produtoRepository.findById(idproduto);
		ModelAndView andView = new ModelAndView("produto/cadastroproduto");
		andView.addObject("produtobj",produto);
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByTable("Produto"));
		
		return andView;
	}
	
	
	@GetMapping("/removerproduto/{idproduto}")
	public ModelAndView excluir(@PathVariable("idproduto") Long idproduto) {
		produtoRepository.deleteById(idproduto);	
		ModelAndView andView = new ModelAndView("produto/lista");
		andView.addObject("produtos", produtoRepository.top10());
		return andView;
	}
	
}