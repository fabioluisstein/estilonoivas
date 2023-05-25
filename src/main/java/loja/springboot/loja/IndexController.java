package loja.springboot.loja;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Empresa;
import loja.springboot.repository.EmpresaRepository;

@Controller
public class IndexController {

	@Autowired
	private EmpresaRepository empresaRepository;

	@RequestMapping("/")
	public String index() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return "principal";
	}
	
	@RequestMapping("/administrativo")
	public ModelAndView indexSistma() {
		Optional<Empresa> empresa = empresaRepository.findById(1L);
		ModelAndView andView = new ModelAndView("administrativo");
		andView.addObject("empresa", empresa.get().getNome());
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return andView;
	} 
	
}
