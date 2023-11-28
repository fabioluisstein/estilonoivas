$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-contas-server").DataTable({
		processing: true,
		serverSide: true,
		responsive: true,
		info:true,
		lengthChange:false,
		lengthMenu: [15, 40, 60, -1],
		"order": [0, "desc"],
		language: {
			"emptyTable": "Nenhum registro encontrado",
			"info": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
			"infoFiltered": "(Filtrados de _MAX_ registros)",
			"infoThousands": ".",
			"loadingRecords": "Carregando...",
			"zeroRecords": "Nenhum registro encontrado",
			"search": "Pesquisar",
			"lengthMenu": " Mostrar  _MENU_ por página",
			"zeroRecords": "Sem resultados",
			"info": "Página _PAGE_ de _PAGES_",
			"paginate": {
				"next": "Próximo",
				"previous": "Anterior",
				"first": "Primeiro",
				"last": "Último"
			},
			"aria": {
				"sortAscending": ": Ordenar colunas de forma ascendente",
				"sortDescending": ": Ordenar colunas de forma descendente"
			}
		},

		ajax: {
			url: "/serverContas",
			data: "data",
			 error : function(e) {
				window.location.href = "/listacontasBancarias";
			 }

			
		},
		  columns: [	

			{ "data": 'id',
			   "width": '5%',
			   render: function ( data, type, row) {	
			    return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="/editarconta/'+row.id+'" class="btn btn-info"><i class="fas fa-edit"></i></a> <div> </div>  <a href="/removerconta/'+row.id+'" class="btn btn-danger"><i id="excluir" class="fas fa-trash"></i></a> </div> </td>';
		       }
		    },

			{ "data": 'id'
		    },
			{ "data": 'instituicao'
		    },
			{ "data": 'tipo'
		    },
			{ "data": 'valor' ,
			   render: function ( valor, type, row) {	
			    return  valor.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'});
		      }
	      },

			{ "data": 'data',
			   render: function ( data, type, row) {	
			    return  moment(row.data).format('L');
		     }
		    }
		],
		dom: 'Bfrtip',
		buttons: [
			{ extend: 'excel',
			  text: 'Excel',
			  exportOptions: {
				columns: ':not(:first-child)',
			  }
			},
			{
			  extend: 'pdf',
			  text: 'PDF',
			  exportOptions: {
				columns: ':not(:first-child)',
			  }
			},
			{
			  extend: 'print',
			  text: 'Imprimir',
			   exportOptions: {
				columns: ':not(:first-child)',
			   }
			},
			{
			  extend: 'colvis',
			  text: 'Colunas',
			   exportOptions: {
				columns: ':not(:first-child)',
			   }
			}
        ],
	});



// acao do botao de excluir (abrir modal)
$("#btn-excluir").on('click', function () {
	if (isSelectedRow()) {
		$("#modal-baixar").modal('show');
	}
});

// exclusao de uma promocao
$("#btn-del-modal").on('click', function () {
	var id = getPromoId();
	$.ajax({
		method: "GET",
		url: "/liberarProdutoAjax/" + id,
		success: function () {
			$("#modal-baixar").modal('hide');
			table.buttons().disable();
			table.ajax.reload();
		},
		error: function () {
			alert("Ops... Ocorreu um erro, tente mais tarde.");
		}
	});
});	

});





