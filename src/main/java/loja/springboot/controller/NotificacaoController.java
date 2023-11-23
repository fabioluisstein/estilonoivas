package loja.springboot.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import loja.springboot.model.Emissor;
import loja.springboot.repository.PromocaoRepository;
import loja.springboot.service.NotificacaoService;


@Controller
public class NotificacaoController {
	
	public void garbageCollection() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

	@Autowired
	private PromocaoRepository repository;
	@Autowired
	private NotificacaoService service;

	@GetMapping("/promocao/notificacao")
	public SseEmitter enviarNotificacao() throws IOException {

		  if( service.getEmissores().size() < 9){
			SseEmitter emitter = new SseEmitter(100000L);
			Emissor emissor = new Emissor(emitter, getDtCadastroUltimaPromocao());
			service.onOpen(emissor);
			service.addEmissor(emissor);
			System.out.println("> size after add: " + service.getEmissores().size());
			garbageCollection();
			emissor.getEmitter().onCompletion(() -> service.removeEmissor(emissor));
			emitter.onTimeout(() -> service.removeEmissor(emissor));

		
			return emissor.getEmitter();
		}
	
	return  new SseEmitter(0L);
	}
	
	private LocalDateTime getDtCadastroUltimaPromocao() {
			garbageCollection();
		return repository.findPromocaoComUltimaData();
	}
}
