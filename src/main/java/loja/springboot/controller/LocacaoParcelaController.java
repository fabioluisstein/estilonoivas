package loja.springboot.controller;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Parcela;
import loja.springboot.repository.ParcelaRepository;

@Controller
public class LocacaoParcelaController {
 

	@Autowired
	private ParcelaRepository parcelaRepository;

	@GetMapping("/editarParcelaCustom/{idparcela}")
	public ModelAndView editarParcelaCustom(@PathVariable("idparcela") Long idparcela)  {
		Optional<Parcela> parcela = parcelaRepository.findById(idparcela); 
		ModelAndView andView = new ModelAndView("locacao/locacaoPagamento");
		andView.addObject("locacaobj",parcela.get().getLocacao());
		andView.addObject("parcelabj", parcela);	
		return andView;
	}
	
	@CacheEvict(value="locacoes",allEntries=true)
	@RequestMapping(method = RequestMethod.POST, value = "salvarparcelaCustom", consumes = { "multipart/form-data" })
	public String salvarparcelaCustom(Parcela parcela, final MultipartFile file) throws IOException {

		if (parcela.getId() == null) {
			if (file.getSize() > 0) {
				parcela.setArquivo(file.getBytes());
				parcela.setTipoArquivo(file.getContentType());
				parcela.setNomeArquivo(file.getOriginalFilename());

			}
			parcelaRepository.saveAndFlush(parcela); 
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

		   parcelaRepository.saveAndFlush(parcela);
		}
		return "redirect:/editarlocacao/"+parcela.getLocacao().getId().toString()+"";
	} 
	

	public boolean verificaImagem(MultipartFile file, byte[] imagem) {
		if (file != null && file.getSize() > 0) {

		}
		return true;

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

		}

	}


	
}