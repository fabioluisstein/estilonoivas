package loja.springboot.controller;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loja.springboot.model.Pagamento;
import loja.springboot.repository.CategoriaRepository;
import loja.springboot.repository.FornecedorRepository;
import loja.springboot.repository.PagamentoRepository;
import loja.springboot.repository.PainelRepository;
import loja.springboot.repository.PainelRepository.listPainelOperacional;
import loja.springboot.service.PagamentoDataTablesService;

@Controller
public class PagamentoController {

	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	private String uploadDir = "C:\\pagamento";
	
	private listPainelOperacional operacional;

    @Autowired
	private PainelRepository painelRepository;

	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
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

 
	@PostMapping("/pesquisarpagamento")
	public ModelAndView pesquisar(@RequestParam("dataInicio") String dataInicio,@RequestParam("dataFinal") String dataFinal) {
		ModelAndView modelAndView = new ModelAndView("pagamento/lista");
		if (dataInicio.isEmpty() && dataFinal.isEmpty()) {
			modelAndView.addObject("pagamentos", pagamentoRepository.findAllPagamentosTodos());
		}
		if (!dataInicio.isEmpty() && !dataFinal.isEmpty()) {
			modelAndView.addObject("pagamentos", pagamentoRepository.findPagamentoDatas(dataInicio, dataFinal));
			return modelAndView;
		}
		modelAndView.addObject("pagamentos", pagamentoRepository.findAllPagamentosTodos());
		garbageCollection();
		return modelAndView;
	}


	@RequestMapping(method = RequestMethod.GET, value = "cadastropagamento")
	public ModelAndView cadastro(Pagamento pagamento) {
		ModelAndView modelAndView = new ModelAndView("pagamento/cadastropagamentos");
		modelAndView.addObject("pagamentobj", new Pagamento());
		modelAndView.addObject("fornecedores", fornecedorRepository.fornecedoresTodos());
		modelAndView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		modelAndView.addObject("id", "Cadastrando Pagamento");
		modelAndView.addObject("color", "alert alert-dark");
		garbageCollection();
		return base(modelAndView);	 
	}

	@RequestMapping(method = RequestMethod.POST, value = "salvarpagamento", consumes = { "multipart/form-data" })
	public ModelAndView salvar(Pagamento pagamento, final MultipartFile file2) throws IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamentos");
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		andView.addObject("id", "Gravado com Sucesso");
		andView.addObject("color", "alert alert-success");
		Pagamento pag = pagamentoRepository.save(pagamento);
		andView.addObject("pagamentobj", pag);

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
					pagamentoRepository.save(pag);
				
				}
			

			} catch(Exception ex){

			}

		}
		garbageCollection();
		return base(andView);	 
	}


	@Transactional(readOnly = false) 
    public void converte(){

             byte[] byteArrayList;
			 String extensao;
			 String name; 

     List<Pagamento> pagamentos = pagamentoRepository.findAll();

	 for (Pagamento pag : pagamentos) {
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
		pagamentoRepository.saveAndFlush(pag);

        }
		 

	}
}




    public ResponseEntity<Resource> convertFile(Long fileId, String tipo) {
        // Ler o arquivo do banco de dados
    Optional<Pagamento> pagamento = pagamentoRepository.findById(fileId);
	String extensao = "." + pagamento.get().getTipoArquivo().substring(pagamento.get().getTipoArquivo().lastIndexOf("/") + 1);
        byte[] fileData = pagamento.get().getArquivo();	
        
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Salvar o arquivo no servidor de arquivos
        try {
			Pagamento pag = pagamentoRepository.getById(fileId);
			pag.setArquivoPath(fileId + extensao);

            pagamentoRepository.save(pag);
            Path filePath = Paths.get(uploadDir).resolve(fileId + extensao); // Altere a extensão conforme necessário
            Files.write(filePath, fileData);
            
            // Criar um recurso do Spring Resource para o arquivo salvo
            Resource resource = new UrlResource(filePath.toUri()); 

            // Preparar uma resposta HTTP com o arquivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

	
 
@GetMapping("baixarArquivoPagamento/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id ) {
        @SuppressWarnings("null")
		String fileName = pagamentoRepository.findById(id).get().getArquivoPath();
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





	@GetMapping("/baixarArquivoPagamento2/{idpagamento}")
	public void baixarArquivo(@PathVariable("idpagamento") Long idpagamento,
			HttpServletResponse response) throws IOException {

		/* Consultar o obejto pessoa no banco de dados */
		Pagamento pagamento = pagamentoRepository.findById(idpagamento).get();
		if (pagamento.getArquivo() != null) {

			/* Setar tamanho da resposta */
			response.setContentLength(pagamento.getArquivo().length);

			/*
			 * Tipo do arquivo para download ou pode ser generica application/octet-stream
			 */
			response.setContentType(pagamento.getTipoArquivo());

			/* Define o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pagamento.getNomeArquivo());
			response.setHeader(headerKey, headerValue);

			/* Finaliza a resposta passando o arquivo */
			response.getOutputStream().write(pagamento.getArquivo());
			garbageCollection();
		}

	}

	@GetMapping("/editarpagamento/{idpagamento}")
	public ModelAndView editar(@PathVariable("idpagamento") Pagamento pagamento) throws ParseException, IOException {
		ModelAndView andView = new ModelAndView("pagamento/cadastropagamentos");
		grafico();
		andView.addObject("pagamentobj", pagamento);
		andView.addObject("fornecedores", fornecedorRepository.findAll());
		andView.addObject("categorias", categoriaRepository.findCategoriaByOriginal("Pagamento"));
		andView.addObject("id", "Editando Registro");
		andView.addObject("color", "alert alert-primary");
	
		garbageCollection();
		return base(andView);
	}

	@GetMapping("/removerpagamento/{idpagamento}")
	public String excluir(@PathVariable("idpagamento") Long idpagamento) {
		try {
		pagamentoRepository.deleteById(idpagamento);
	} catch (Exception e) {
	}
	garbageCollection();
	return "redirect:/listapagamentos";
  }


    @GetMapping("/listapagamentos")
	public ModelAndView showTabelas() {
	    ModelAndView andView = new ModelAndView("pagamento/pagamentos-datatables");
		grafico();
	//	converte();
		garbageCollection();
		return base(andView);	 
		} 

/* 
		@GetMapping("/converter")
		public String converternew() {
		 	converte();
			 return "redirect:/listapagamentos";
		
 			} 
*/
   @GetMapping("/serverPagamentos")
		public ResponseEntity<?> datatables(HttpServletRequest request) {
			Map<String, Object> data = new PagamentoDataTablesService().execute(pagamentoRepository, request);
			return ResponseEntity.ok(data);   
	}


}