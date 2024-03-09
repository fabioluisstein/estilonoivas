package loja.springboot.loja;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loja.springboot.controller.ReportUtil;
import loja.springboot.model.Pessoa;
import loja.springboot.repository.ContaBancariaRepository;
import loja.springboot.repository.GraficoRepository;
import loja.springboot.repository.GraficoRepository.listGraficoCard;
import loja.springboot.repository.GraficoRepository.listGraficoClienteCidade;
import loja.springboot.repository.GraficoRepository.listGraficoLocacaoAtendente;
import loja.springboot.repository.GraficoRepository.listGraficoMes;
import loja.springboot.repository.GraficoRepository.listGraficoOrigemCLiente;
import loja.springboot.repository.GraficoRepository.listGraficoPapelCliente;
import loja.springboot.repository.GraficoRepository.listGraficoPrincipal;
import loja.springboot.repository.GraficoRepository.listGraficoSecundario;

@Controller
public class IndexController {


	@Autowired
	private GraficoRepository graficoRepository;

	@Autowired
	private ContaBancariaRepository contaBancariaRepository;

	@Autowired
	private ReportUtil reportUtil;

	
	private ModelAndView andView = new ModelAndView("home/index");

	public void permissao() {
		Pessoa p = new Pessoa();
		if (p.obterUsuarioLogado().equalsIgnoreCase("adm")) {
			andView.addObject("permissao", "info-box-number");
		} else {
			andView.addObject("permissao", "invisible");
		}

	}

	@RequestMapping("/")
	public String index() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return "principal";
	}


		@RequestMapping("/login")
	public String login() {
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return "login2";
	}


	// login invalido
	@GetMapping({"/login-error"})
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Crendenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para usuários autorizados.");
		return "login2";
	}


	@GetMapping("/expired")
	public String sessaoExpirada(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Acesso recusado!");
		model.addAttribute("texto", "Sua sessão expirou.");
		model.addAttribute("subtexto", "Você logou em outro dispositivo");
		return "login";
	}	
	
	// acesso negado
	@GetMapping({"/acesso-negado"})
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acesso Negado");
		model.addAttribute("message", "Você não tem permissão para acesso a esta área ou ação.");
		return "error";
	}	

	

	@RequestMapping("/administrativo3")
	public ModelAndView index3() {
		String tabela = "";
		List<listGraficoPrincipal> grafico = graficoRepository.graficoPrincipal();
		List<listGraficoCard> graficoCard = graficoRepository.graficoCard();
		List<listGraficoSecundario> graficoSecundario = graficoRepository.graficoSecundario();
		List<listGraficoClienteCidade> graficoTabela = graficoRepository.graficoClienteCidade();
		for (listGraficoClienteCidade grafico2 : graficoTabela) {
			tabela = tabela + " " + grafico2.getTabela().toString();
		}
		andView.addObject("tabelaClientesCidades", tabela);
		ModelAndView andView = new ModelAndView("template");
		andView.addObject("listacontasBancarias", contaBancariaRepository.listContasBancarias());
		andView.addObject("qtdLocacao", graficoSecundario.get(0).getLocacao());
		andView.addObject("ticket", grafico.get(0).getTicket());
		andView.addObject("indicadorGeral", graficoSecundario.get(0).getIndicador());
		andView.addObject("locadoHoje", graficoCard.get(0).getLocadoHoje());
		andView.addObject("tabelaOrigemClientes", tabela);
		return andView;
	}

	




	

	public void carregaPainelPrinicial() {
		List<listGraficoPrincipal> grafico = graficoRepository.graficoPrincipal();
		andView.addObject("empresa", grafico.get(0).getEmpresa());
		andView.addObject("liquido", grafico.get(0).getLiquido());
		andView.addObject("cliente", grafico.get(0).getCliente());
		andView.addObject("ticket", grafico.get(0).getTicket());
		andView.addObject("valuation", grafico.get(0).getValuation());
	}

	public void carregaPainelCard() {
		List<listGraficoCard> graficoCard = graficoRepository.graficoCard();
		andView.addObject("locacaoFutura", graficoCard.get(0).getLocacaoFutura());
		andView.addObject("locadoHoje", graficoCard.get(0).getLocadoHoje());
		andView.addObject("tempo", graficoCard.get(0).getTempo());
		andView.addObject("prova", graficoCard.get(0).getProva());
		andView.addObject("LocacaoTotal", graficoCard.get(0).getLocacaoTotal());
		andView.addObject("QuantidadeCidades", graficoCard.get(0).getQuantidadeCidades());
		andView.addObject("LocacaoMedia", graficoCard.get(0).getLocacaoMedia());
		andView.addObject("NaoLocado", graficoCard.get(0).getNaolocados());
		andView.addObject("ValorCaixa", graficoCard.get(0).getValorcaixa()); 
		andView.addObject("SaldoConta", graficoCard.get(0).getSaldoConta());
		andView.addObject("crescimento", graficoCard.get(0).getCrescimento());

	}

	public void carregaPainelEntradasSaidas() {
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Double> data = new ArrayList<Double>();
		String titulo = "Entradas/Saidas Mes";
		String mes = "";
		ArrayList<listGraficoMes> list = new ArrayList<listGraficoMes>();

		list.addAll(graficoRepository.graficoMes());

		for (listGraficoMes listGraficoMes : list) {
			labels.add(listGraficoMes.getTipo());
			data.add(listGraficoMes.getValor());
			mes = listGraficoMes.getMes();
		}

		andView.addObject("labels", labels);
		andView.addObject("titulo", titulo + "/" + mes);
		andView.addObject("data", data);

	}

	public void carregaPainelPapelClientes() {
		ArrayList<String> labelsPapelCliente = new ArrayList<String>();
		ArrayList<Double> dataPapelCliente = new ArrayList<Double>();
		String tituloPapelCliente = "Tipos de Clientes";

		List<listGraficoPapelCliente> graficoTabela = graficoRepository.graficoPapelCliente();

		for (listGraficoPapelCliente graficoPapel : graficoTabela) {
			labelsPapelCliente.add(graficoPapel.getPapel());
			dataPapelCliente.add(graficoPapel.getValor());
		}

		andView.addObject("labelsPapel", labelsPapelCliente);
		andView.addObject("tituloPapel", tituloPapelCliente);
		andView.addObject("dataPapel", dataPapelCliente);
	}

	@RequestMapping("/administrativo")
	public ModelAndView indexSistma2() {

		permissao();
		carregaPainelPrinicial();
		carregaPainelEntradasSaidas();
		carregaPainelCard();
		carregaPainelPapelClientes();
		tabelaOrigemCliente();
		tabelaClienteCidade();
		tabelaLocacaoAtendente();
		carregaCardSecundario();
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
		return andView;
	}

	public void carregaCardSecundario() {
		List<listGraficoSecundario> graficoSecundario = graficoRepository.graficoSecundario();
		andView.addObject("qtdLocacao", graficoSecundario.get(0).getLocacao());
		andView.addObject("oportunidade", graficoSecundario.get(0).getOportunidades());
		andView.addObject("indice", graficoSecundario.get(0).getIndice());
		andView.addObject("eventos", graficoSecundario.get(0).getEventoFuturos());
		andView.addObject("cac", graficoSecundario.get(0).getCac());
		andView.addObject("indicadorGeral", graficoSecundario.get(0).getIndicador());
		andView.addObject("percentualAds", graficoSecundario.get(0).getPercentualAds());
		andView.addObject("NegociosPerdidos", graficoSecundario.get(0).getNegociosPerdidos());

	}

	public void tabelaOrigemCliente() {
		List<listGraficoOrigemCLiente> graficoTabela = graficoRepository.graficoOrigemCliente();
		String tabela = "";
		for (listGraficoOrigemCLiente grafico : graficoTabela) {
			tabela = tabela + " " + grafico.getTabela().toString();
		}
		andView.addObject("tabelaOrigemClientes", tabela);
	}

	public void tabelaClienteCidade() {
		List<listGraficoClienteCidade> graficoTabela = graficoRepository.graficoClienteCidade();
		String tabela = "";
		for (listGraficoClienteCidade grafico : graficoTabela) {
			tabela = tabela + " " + grafico.getTabela().toString();
		}
		andView.addObject("tabelaClientesCidades", tabela);
	}

	public void tabelaLocacaoAtendente() {
		List<listGraficoLocacaoAtendente> graficoTabela = graficoRepository.graficoLocacaoAtendente();
		String tabela = "";
		for (listGraficoLocacaoAtendente grafico : graficoTabela) {
			tabela = tabela + " " + grafico.getTabela().toString();
		}
		andView.addObject("tabelaLocacaoAtendente", tabela);
	}

	@RequestMapping("etiquetas/gerar")
	public void imprimePdfEtiquetas(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		byte[] pdf = reportUtil.gerarRelatorio("etiqueta", null, request.getServletContext());
		response.setContentLength(pdf.length);
		// envia a resposta com o MIME Type
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "etiquetas.pdf");
		response.setHeader(headerKey, headerValue);
		response.getOutputStream().write(pdf);
		// garbageCollection();
		Runtime.getRuntime().gc();
		Runtime.getRuntime().freeMemory();
	}

}
