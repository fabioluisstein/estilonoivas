package loja.springboot.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import loja.springboot.model.Parcela;
import loja.springboot.model.Pessoa;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.repository.ParcelaRepository;

@Controller 
public class LocacaoParcelaController {
 
	@Autowired
	private ParcelaRepository parcelaRepository;

    @Autowired
	private PainelRepository painelRepository;

	private listPainelOperacional operacional;

	
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

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	@GetMapping("/editarParcelaCustom/{idparcela}")
	public ModelAndView editarParcelaCustom(@PathVariable("idparcela") Parcela parcela)  {
		grafico();
		ModelAndView andView = new ModelAndView("locacao/locacaoPagamentos");
			Pessoa p = new Pessoa();
		if (p.obterUsuarioLogado().equalsIgnoreCase("adm")) {
			andView.addObject("seguranca",false);
		}
		else{
				andView.addObject("seguranca",true);
		}
		andView.addObject("locacaobj",parcela.getLocacao());
		andView.addObject("parcelabj", parcela);	
		garbageCollection();
		return base(andView);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "salvarparcelaCustom", consumes = { "multipart/form-data" })
	public String salvarparcelaCustom(Parcela parcela, final MultipartFile file) throws IOException {

		if (parcela.getId() == null) {
			if (file.getSize() > 0) {
				parcela.setArquivo(file.getBytes());
				parcela.setTipoArquivo(file.getContentType());
				parcela.setNomeArquivo(file.getOriginalFilename());

			}
			parcelaRepository.saveAndFlush(parcela); 
			garbageCollection();
			return "redirect:/editarlocacao/"+parcela.getLocacao().getId().toString()+"";
		}

		if (parcela.getId() != null) {
			Optional<Parcela> pdt = parcelaRepository.findById(parcela.getId());
			if (file.getSize() > 0) {
				parcela.setArquivo(file.getBytes());
				parcela.setTipoArquivo(file.getContentType());
				parcela.setNomeArquivo(file.getOriginalFilename());
			}

			if (pdt.get().getArquivo() != null && file.getSize() == 0) {
				parcela.setArquivo(pdt.get().getArquivo());
				parcela.setTipoArquivo(pdt.get().getTipoArquivo());
				parcela.setNomeArquivo(pdt.get().getNomeArquivo());

			}

		   parcelaRepository.save(parcela);
		   garbageCollection();
		}
		return "redirect:/editarlocacao/"+parcela.getLocacao().getId().toString()+"";
	} 
	

	@GetMapping("/baixarArquivoParcela/{idparcela}")
	public void baixarArquivoParcela(@PathVariable("idparcela") Long idparcela,
			HttpServletResponse response) throws IOException {

		/* Consultar o obejto pessoa no banco de dados */
		Parcela parcela = parcelaRepository.findById(idparcela).get();
		if (parcela.getArquivo() != null) {

			/* Setar tamanho da resposta */
			response.setContentLength(parcela.getArquivo().length);

			/*
			 * Tipo do arquivo para download ou pode ser generica application/octet-stream
			 */
			response.setContentType(parcela.getTipoArquivo());

			/* Define o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", parcela.getNomeArquivo());
			response.setHeader(headerKey, headerValue);

			/* Finaliza a resposta passando o arquivo */
			response.getOutputStream().write(parcela.getArquivo());
			garbageCollection();

		}

	}


	
}