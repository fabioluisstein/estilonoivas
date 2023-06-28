package loja.springboot.loja;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import loja.springboot.model.Empresa;
import loja.springboot.repository.EmpresaRepository;
import loja.springboot.repository.GraficoRepository;
import loja.springboot.repository.GraficoRepository.listGraficoCard;
import loja.springboot.repository.GraficoRepository.listGraficoMes;
import loja.springboot.repository.GraficoRepository.listGraficoOrigemCLiente;
import loja.springboot.repository.GraficoRepository.listGraficoPapelCliente;
import loja.springboot.repository.GraficoRepository.listGraficoPrincipal;
import loja.springboot.repository.GraficoRepository.listGraficoSecundario;

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
		andView.addObject("tabelaOrigemClientes", 		tabelaOrigemCliente());
      


		List<listGraficoCard> graficoCard = graficoRepository.graficoCard();

		andView.addObject("locacaoFutura", graficoCard.get(0).getLocacaoFutura());
		andView.addObject("locadoHoje", graficoCard.get(0).getLocadoHoje());
		andView.addObject("tempo", graficoCard.get(0).getTempo());
		andView.addObject("prova", graficoCard.get(0).getProva());


		ArrayList<String> labels = new ArrayList<String>(); 
        ArrayList<Double> data = new ArrayList<Double>(); 
        String titulo = "Entradas/Saidas Mes";
        String mes = "";
        ArrayList< listGraficoMes> list = new ArrayList<listGraficoMes>(); 

        list.addAll( graficoRepository.graficoMes());

        for (listGraficoMes listGraficoMes : list) {
            labels.add(listGraficoMes.getTipo());
            data.add(listGraficoMes.getValor());
            mes = listGraficoMes.getMes();
            }
            
			andView.addObject("labels", labels);
			andView.addObject("titulo", titulo+"/"+mes);
			andView.addObject("data", data);
                                    

			ArrayList<String> labelsPapelCliente = new ArrayList<String>(); 
			ArrayList<Double> dataPapelCliente = new ArrayList<Double>(); 
			String tituloPapelCliente = "Tipos de Clientes";
	
			List<listGraficoPapelCliente> graficoTabela =graficoRepository.graficoPapelCliente();
			
			for (listGraficoPapelCliente graficoPapel : graficoTabela) {
				labelsPapelCliente.add(graficoPapel.getPapel()) ;
				dataPapelCliente.add(graficoPapel.getValor());
			 }

				andView.addObject("labelsPapel", labelsPapelCliente);
				andView.addObject("tituloPapel", tituloPapelCliente);
				andView.addObject("dataPapel", dataPapelCliente);

			
			List<listGraficoSecundario> graficoSecundario = graficoRepository.graficoSecundario();
			andView.addObject("qtdLocacao", graficoSecundario.get(0).getLocacao());
		    andView.addObject("oportunidade", graficoSecundario.get(0).getOportunidades());
			andView.addObject("indice", graficoSecundario.get(0).getIndice());
			andView.addObject("eventos", graficoSecundario.get(0).getEventoFuturos());

		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return andView;
	}  
	   


public String tabelaOrigemCliente(){
	List<listGraficoOrigemCLiente> graficoTabela = graficoRepository.graficoOrigemCliente();
		String tabela = "";
	for (listGraficoOrigemCLiente grafico : graficoTabela) {
		tabela  = tabela  + " " + 	grafico.getTabela().toString();
	 }
	  return tabela;
}


}
 