package loja.springboot.service;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import loja.springboot.repository.ParcelaRepository;
import loja.springboot.repository.ParcelaRepository.listParcelasNf;

public class ParcelaService {
 
	private String[] cols = {
		"stparcela", "id", "locacao", "cliente", "cpf", "cidade", "nf", "observacao", "pagamento", "vencimento", "moeda", "valor","atendente", "banco"};
	
	public Map<String, Object> execute(ParcelaRepository repository, HttpServletRequest request) {
		
		int start = Integer.parseInt(request.getParameter("start"));
		int lenght = Integer.parseInt(request.getParameter("length"));
		int draw = Integer.parseInt(request.getParameter("draw"));
		
		int current = currentPage(start, lenght);
		
		String column = columnName(request);
		Sort.Direction direction = orderBy(request);
		String search = searchBy(request);
		
		Pageable pageable = PageRequest.of(current, lenght, direction, column);
		
		Page<listParcelasNf> page = queryBy(search, repository, pageable);
		
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw);
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		json.put("data", page.getContent());
		
		return json;
	}

	@Transactional(readOnly = true)
	private Page<listParcelasNf> queryBy(String search, ParcelaRepository repository, Pageable pageable) {		
		return repository.findByParcela(search, pageable);
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
