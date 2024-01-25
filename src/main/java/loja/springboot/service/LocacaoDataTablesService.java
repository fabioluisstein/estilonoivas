package loja.springboot.service;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import loja.springboot.repository.LocacaoRepository;
import loja.springboot.repository.LocacaoRepository.listaLocacoesGerais;;

public class LocacaoDataTablesService {


	private String[] cols = {
		"id","data_locacao", "total_produto","falta_pagar", "cliente","cidade", "data_retirada", "telefone","whats", "Contrato","Operacao"
	};  
	 
	public Map<String, Object> execute(LocacaoRepository repository, HttpServletRequest request, int valor) {
		
		int start = Integer.parseInt(request.getParameter("start")); 
		int lenght = Integer.parseInt(request.getParameter("length"));
		int draw = Integer.parseInt(request.getParameter("draw"));
		
		int current = currentPage(start, lenght);
		
		String column = columnName(request);
		Sort.Direction direction = orderBy(request);
		String search = searchBy(request);
		
		Pageable pageable = PageRequest.of(current, lenght, direction, column);
		
		Page<listaLocacoesGerais> page = queryBy(search, repository, pageable, valor); 
		
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw);
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		json.put("data", page.getContent());
		
		return json;
	}

	private Page<listaLocacoesGerais> queryBy(String search, LocacaoRepository repository, Pageable pageable, int valor) {	
		if(valor == 0) { 	
	      return repository.findByLocacoes(search, pageable);
	    } else 
	       return repository.findByLocacoesVencidas(search, pageable);
	}

	private String searchBy(HttpServletRequest request) {
		
		return request.getParameter("search[value]").isEmpty()
				? ""
				: request.getParameter("search[value]");
	}	

	private Direction orderBy(HttpServletRequest request) {
		String order = request.getParameter("order[0][dir]");
		Sort.Direction sort = Sort.Direction.ASC;
		if (order.equalsIgnoreCase("desc")) {
			sort = Sort.Direction.DESC;
		}
		return sort;
	}

	private String columnName(HttpServletRequest request) {
		int iCol = Integer.parseInt(request.getParameter("order[0][column]"));
		return cols[iCol];
	}

	private int currentPage(int start, int lenght) {
		//0			1			2
		//0-9 |	10-19 	| 20-29
		return start / lenght;
	}
	
}
