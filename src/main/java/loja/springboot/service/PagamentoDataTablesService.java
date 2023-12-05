package loja.springboot.service;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import loja.springboot.repository.PagamentoRepository;
import loja.springboot.repository.PagamentoRepository.listaSaidasGerais;;

public class PagamentoDataTablesService {

	private String[] cols = {
		"id", "tipo", "data", "fornecedor", "moeda", "origem", "valor"
	};  
	 
	public Map<String, Object> execute(PagamentoRepository repository, HttpServletRequest request) {
		
		int start = Integer.parseInt(request.getParameter("start"));
		int lenght = Integer.parseInt(request.getParameter("length"));
		int draw = Integer.parseInt(request.getParameter("draw"));
		
		int current = currentPage(start, lenght);
		
		String column = columnName(request);
		Sort.Direction direction = orderBy(request);
		String search = searchBy(request);
		
		Pageable pageable = PageRequest.of(current, lenght, direction, column);
		
		Page<listaSaidasGerais> page = queryBy(search, repository, pageable); 
		
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw);
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		json.put("data", page.getContent());
		
		return json;
	}

	private Page<listaSaidasGerais> queryBy(String search, PagamentoRepository repository, Pageable pageable) {		
		return repository.findByPagamento(search, pageable);
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
