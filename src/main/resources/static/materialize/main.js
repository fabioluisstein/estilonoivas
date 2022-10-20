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

// Tooltip
const elemsTooltip = document.querySelectorAll(".tooltipped");
const instanceTooltip = M.Tooltip.init(elemsTooltip, {
    html: "Olha essa dica!",
    position: "right"
});



$("#valorCompra").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
$("#valorVenda").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});

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


$(document).ready(function(){	
	$('.datepicker').datepicker({
	    format: 'dd/mm/yyyy',
		 i18n: {
			 today: 'Hoje',
			    clear: 'Limpar',
			 months: [ 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro' ],
		        monthsShort: [ 'Jan', 'Fev', 'Mar', 'Abr', 'Maio', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez' ],
		        weekdaysFull: [ 'Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado' ],
		        weekdaysShort: [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb' ],
             weekdaysAbbrev: ["D","S", "T", "Q", "Q", "S", "S"],
         
         }
	
	});

});