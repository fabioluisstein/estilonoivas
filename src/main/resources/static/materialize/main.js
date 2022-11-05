// Floating Action Button
const elemsBtns = document.querySelectorAll(".fixed-action-btn");
const floatingBtn = M.FloatingActionButton.init(elemsBtns, {
    direction: "left",
    hoverEnabled: false
});

// Navbar
const elemsDropdown = document.querySelectorAll(".dropdown-trigger");
const instancesDropdown = M.Dropdown.init(elemsDropdown, {
    coverTrigger: false
});
const elemsSidenav = document.querySelectorAll(".sidenav");
const instancesSidenav = M.Sidenav.init(elemsSidenav, {
    edge: "left"
});

// Modal
const elemsModal = document.querySelectorAll(".modal");
const instancesModal = M.Modal.init(elemsModal);


function openModal(id) {
  
	
		
	  $.ajax({
			method : "GET",
			url : "/buscarparcelaid",
			data : "idparcela="+id,
			success : function(response) {


			
				$("#id_parcela").val(response.id);
				$("#observacaoParcela").val(response.observacao);
				$("#valor").val(response.valor);
				$("#data_vencimento").val(response.data_vencimento);
				$("#data_pagamento").val(response.data_pagamento);
				$("#moeda").val(response.moeda);
				 
				$("#locacao").val(response.locacao);
				  
				
				document.getElementById("btnModal").click();
			}
		}).fail(function(xhr, status, errorThrown) {
			alert("Erro ao buscar usuario por id: " + xhr.responseText);
		});
	  
	  
}
	
	




function consultaPrecoProduto(id) {
	  
	

	
	
	var id = $("#produto").val();
	

	
	  $.ajax({
			method : "GET",
			url : "/buscarprodutoid",
			data : "idproduto="+id,
			success : function(response) {

			
				$("#valorProduto").val(response.valorVenda);
				
			}
		}).fail(function(xhr, status, errorThrown) {
			alert("Erro ao buscar produto por id: " + xhr.responseText);
		});
	  
	  
}
	
	



// Tooltip
const elemsTooltip = document.querySelectorAll(".tooltipped");
const instanceTooltip = M.Tooltip.init(elemsTooltip, {
    html: "Olha essa dica!",
    position: "right"
});


$(document).ready(function(){
    $('.collapsible').collapsible();
  });


$("#valorCompra").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
$("#valorVenda").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
$("#valor").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
$("#valorProduto").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});


const formatter = new Intl.NumberFormat('pt-BR', {
    currency : 'BRL',
    minimumFractionDigits : 2
});
$("#valorCompra").val(formatter.format($("#valorCompra").val()));
$("#valorCompra").focus();
$("#valorCompra").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});

$("#valorVenda").val(formatter.format($("#valorVenda").val()));
$("#valorVenda").focus();
$("#valorVenda").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});

$("#valor").val(formatter.format($("#valor").val()));
$("#valor").focus();
$("#valor").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});


$("#valorProduto").val(formatter.format($("#valorProduto").val()));
$("#valorProduto").focus();
$("#valorProduto").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});



$(document).ready(function(){	
	$('.datepicker').datepicker({
	    format: 'dd/mm/yyyy',
		 i18n: {
			 today: 'Hoje',
			 container: 'body',
			    clear: 'Limpar',
			 months: [ 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro' ],
		        monthsShort: [ 'Jan', 'Fev', 'Mar', 'Abr', 'Maio', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez' ],
		        weekdaysFull: [ 'Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado' ],
		        weekdaysShort: [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb' ],
             weekdaysAbbrev: ["D","S", "T", "Q", "Q", "S", "S"],
         
         }
	
	});

});