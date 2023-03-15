package loja.springboot.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Locacao;
import loja.springboot.model.LocacaoProduto;
import loja.springboot.model.Parcela;
import loja.springboot.repository.LocacaoProdutoRepository;
import loja.springboot.repository.LocacaoRepository;
import loja.springboot.repository.ProdutoRepository;

@Controller
public class LocacaoProdutoController {
 

	@Autowired
	private LocacaoRepository locacaoRepository;
	
	@Autowired
	private LocacaoProdutoRepository locacaoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	 



	@GetMapping("/editarProdutoCustom/{idproduto}")
	public ModelAndView editarParcelaCustom(@PathVariable("idproduto") Long idproduto)  {
		Optional<LocacaoProduto> locacaoproduto = locacaoProdutoRepository.findById(idproduto); 
		ModelAndView andView = new ModelAndView("locacao/locacaoProduto");
		andView.addObject("locacao",locacaoproduto.get().getLocacao());
		andView.addObject("produtobj", locacaoproduto);	
		andView.addObject("produtos", produtoRepository.findAll());
		return andView;
	}


	@RequestMapping(method = RequestMethod.POST, value = "salvarprodutoCustom", consumes = { "multipart/form-data" })
	public String salvarprodutoCustom(LocacaoProduto produtoLocaocao, final MultipartFile file) throws IOException {

		if (produtoLocaocao.getId() == null) {
			if (file.getSize() > 0) {
				produtoLocaocao.setArquivo(file.getBytes());
				produtoLocaocao.setTipoArquivo(file.getContentType());
				produtoLocaocao.setNomeArquivo(file.getOriginalFilename());

			}
			locacaoProdutoRepository.saveAndFlush(produtoLocaocao); 
			return "redirect:/editarlocacao/"+produtoLocaocao.getLocacao().getId().toString()+"";
		
		}

		if (produtoLocaocao.getId() != null) {
			Optional<LocacaoProduto> pdt = locacaoProdutoRepository.findById(produtoLocaocao.getId());
			if (file.getSize() > 0) {
				produtoLocaocao.setArquivo(file.getBytes());
				produtoLocaocao.setTipoArquivo(file.getContentType());
				produtoLocaocao.setNomeArquivo(file.getOriginalFilename());
			}

			if (pdt.get().getArquivo() != null && file.getSize() == 0) {
				produtoLocaocao.setArquivo(pdt.get().getArquivo());
				produtoLocaocao.setTipoArquivo(pdt.get().getTipoArquivo());
				produtoLocaocao.setNomeArquivo(pdt.get().getNomeArquivo());

			}

			locacaoProdutoRepository.saveAndFlush(produtoLocaocao);
		}
		return "redirect:/editarlocacao/"+produtoLocaocao.getLocacao().getId().toString()+"";
	} 
	

	public boolean verificaImagem(MultipartFile file, byte[] imagem) {
		if (file != null && file.getSize() > 0) {

		}
		return true;

	}

	@GetMapping("/baixarArquivoProdutoCustom/{idproduto}")
	public void baixarArquivoProdutoCustom(@PathVariable("idproduto") Long idproduto,
			HttpServletResponse response) throws IOException {

		/* Consultar o obejto pessoa no banco de dados */
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findById(idproduto).get();
		if (locacaoProduto.getArquivo() != null) {

			/* Setar tamanho da resposta */
			response.setContentLength(locacaoProduto.getArquivo().length);

			/*
			 * Tipo do arquivo para download ou pode ser generica application/octet-stream
			 */
			response.setContentType(locacaoProduto.getTipoArquivo());

			/* Define o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", locacaoProduto.getNomeArquivo());
			response.setHeader(headerKey, headerValue);

			/* Finaliza a resposta passando o arquivo */
			response.getOutputStream().write(locacaoProduto.getArquivo());

		}

	}

	@GetMapping("/editarprodutolocacaoCustoms/{idproduto}")
	public ModelAndView editarProduto(@PathVariable("idproduto") Long idproduto)  {
		Optional<LocacaoProduto> locacaoProduto = locacaoProdutoRepository.findById(idproduto);
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		andView.addObject("locacaobj",locacaoProduto.get().getLocacao());
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("produtos", produtoRepository.findAll());
		return andView;
	} 

	@GetMapping("/editarprodutolocacaoCustom/{idproduto}")
	public ModelAndView edit(@PathVariable("idproduto") Long idproduto)  {
		ModelAndView andView = new ModelAndView("/listaAjustes");
    return andView;
	} 

	/**
	 * @param idprodutoLocacao
	 * @return
	 */
	@GetMapping("/liberarProduto/{idprodutoLocacao}")
	public ModelAndView liberacaoProduto(@PathVariable("idprodutoLocacao") Long idprodutoLocacao)  {
		LocacaoProduto locacaoProduto = locacaoProdutoRepository.findProdutoLocaoById(idprodutoLocacao).get(0);
		locacaoProduto.setData_liberacao(new Date());
		locacaoProdutoRepository.saveAndFlush(locacaoProduto);
		ModelAndView andView = new ModelAndView("produto/ajustes");
		andView.addObject("produtosContrato", locacaoProdutoRepository.locacoesProdutos()); 
		
    return andView;
	} 
	
	@RequestMapping(method = RequestMethod.POST, value ="salvarprodutoCustom")
	public String salvarProdutoCustom(LocacaoProduto produtoLocacao) throws IOException {	
		 locacaoProdutoRepository.saveAndFlush(produtoLocacao);
		    return "redirect:/voltar/"+produtoLocacao.getLocacao().getId().toString()+"";
	} 
	
	@GetMapping("/voltarCustom/{idlocacao}")
	public ModelAndView voltar(@PathVariable("idlocacao") Long idlocacao)  {
		ModelAndView andView = new ModelAndView("locacao/cadastrolocacao");
		Optional<Locacao> locacao = locacaoRepository.findById(idlocacao);
		Parcela parcela = new Parcela();
		LocacaoProduto locacaoProduto = new LocacaoProduto();
		locacaoProduto.setLocacao(locacao.get());
	    parcela.setLocacao(locacao.get()); 
	    andView.addObject("locacaobj",locacao);
		andView.addObject("parcelabj", parcela);
		andView.addObject("produtobj", locacaoProduto);
		andView.addObject("parcelas", locacao.get().getParcelas());
		andView.addObject("produtosLocacoes", locacao.get().getProdutos());	
		andView.addObject("produtos", produtoRepository.findAll());	
		return andView;
	}
	
	


	
	
	

	
	
}