package loja.springboot.controller;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import loja.springboot.repository.GraficoRepository;
import loja.springboot.repository.GraficoRepository.listGraficoMes;

@Controller
public class ChartController {

	@Autowired
	private GraficoRepository graficoRepository;
    

    @GetMapping("/chart")
    public String showChart(Model model) {
        // Dados do gráfico
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
            
        model.addAttribute("labels", labels);
        model.addAttribute("titulo", titulo+"/"+mes);
        model.addAttribute("data", data);

        return "chart";
    }
}