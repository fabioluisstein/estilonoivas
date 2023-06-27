package loja.springboot.loja;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Empresa;
import loja.springboot.repository.EmpresaRepository;
import loja.springboot.repository.GraficoRepository;
import loja.springboot.repository.GraficoRepository.listGraficoPrincipal;

@Controller
public class IndexController {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private GraficoRepository graficoRepository;

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

	@RequestMapping("/administrativo2")
	public ModelAndView indexSistma2() { 
		List<listGraficoPrincipal> grafico = graficoRepository.graficoPrincipal();
		ModelAndView andView = new ModelAndView("home/index");
		andView.addObject("empresa", grafico.get(0).getEmpresa());
		andView.addObject("liquido", grafico.get(0).getLiquido());
		andView.addObject("cliente", grafico.get(0).getCliente());
		andView.addObject("ticket", grafico.get(0).getTicket());
		andView.addObject("valuation", grafico.get(0).getValuation());
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return andView;
	}  
	   
	
}
 