package loja.springboot.controller;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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

	private String uploadDir = "C:\\arquivos\\comprovantes";

	
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
	public String salvarparcelaCustom(Parcela parcela, final MultipartFile file2) throws IOException {
		Parcela pag = parcelaRepository.save(parcela);
	
           if(file2.getSize()>0) {
			try {
				// Salva o arquivo no diretório especificado
		
				if (!file2.isEmpty()) {
					String fileName = file2.getOriginalFilename();
					String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
					// Define o caminho onde a imagem será salva
					String imagePath = uploadDir + "\\" + pag.getId() + "."+ fileExtension;
					// Salva a imagem no servidor de arquivos
			
					byte[] bytes = file2.getBytes();
					Path path = Paths.get(imagePath);
					
					Files.write(path, bytes);
				
					pag.setArquivoPath(pag.getId() + "."+ fileExtension);
					parcelaRepository.save(pag);
				
				}
			

			} catch(Exception ex){

			}

		}
		garbageCollection();
	


		return "redirect:/editarlocacao/"+parcela.getLocacao().getId().toString()+"";
	} 
	



@GetMapping("baixarArquivoParcela/{id}")
    public ResponseEntity<Resource> baixarArquivoParcela(@PathVariable Long id ) {
        @SuppressWarnings("null")
		String fileName = parcelaRepository.findById(id).get().getArquivoPath();
        try {
            // Caminho completo para o arquivo
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            // Verifica se o arquivo existe e é acessível
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }




@Transactional(readOnly = false) 
    public void converte(){

             byte[] byteArrayList;
			 String extensao;
			 String name; 

     List<Parcela> pagamentos = parcelaRepository.findAll();

	 for (Parcela pag : pagamentos) {
       if(pag.getNomeArquivo() != null) {

		byteArrayList = pag.getArquivo();  
		extensao = "." + pag.getTipoArquivo().substring(pag.getTipoArquivo().lastIndexOf("/") + 1);
		name = pag.getId().toString();
		try {

			Path directory = Paths.get(uploadDir);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}
	
			// Itera sobre a lista de byte[] e salva cada um como um arquivo no diretório especificado
		
				String fileName = name + extensao; // Nome do arquivo
				Path filePath = directory.resolve(fileName); // Caminho completo do arquivo
				 
	
				// Salva os dados do arquivo no disco
				try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
					fos.write(byteArrayList);
				}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		pag.setArquivoPath(name + extensao);
		parcelaRepository.saveAndFlush(pag);

        }
		  

	}
}

	/* 
		@GetMapping("/converter")
		public String converternew() {
		 	converte();
			 return "redirect:/listalocacoes";
		
 			} 

*/
	
}