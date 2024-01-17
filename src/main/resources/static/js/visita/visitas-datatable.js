$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-visita-server").DataTable({
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
			url: "/serverVisitas",
			data: "data",
			 error : function(e) {
				window.location.href = "/listavisitas";
			 }

			
		},
		  columns: [	
			{ "data": 'id'
		    },
			{ "data": 'cliente'
		    },
			{ "data": 'atendente'
		    },
			{ "data": 'tpcliente'
		    },
			{ "data": 'status'
		    },
		    { "data": 'origem'
         	},
		    { "data": 'datalimite',
			   render: function ( data, type, row) {	
			    return  moment(row.datalimite).format('L');
		     }
		    },
			
			{ "data": 'id',
			"width": '5%',
			render: function ( data, type, row) {	 
			 return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="/editarvisita/'+row.id+'" class="btn btn-info"><i class="fas fa-edit"></i></a> <div> </div>  <button onclick="if (confirm(\'Deseja excluir?\')) { window.location.href = \'/removervisita/' + row.id +'\'; }  else {  }" class="btn btn-danger"><i class="fas fa-trash"></i></button> </div> </td>';
			}
		 }
		],
		columnDefs: [
			{ targets: [ 3 ], orderable: false },	
		  ],
		dom: 'Bfrtip',
		buttons: [
			{ extend: 'excel',
			  text: 'Excel',
			  exportOptions: {
				columns: ':not(:last-child)',
			  }
			},
			{
			  extend: 'pdf',
			  text: 'PDF',
			  exportOptions: {
				columns: ':not(:last-child)',
			  }
			},
			{
			  extend: 'print',
			  text: 'Imprimir',
			   exportOptions: {
				columns: ':not(:last-child)',
			   }
			},
			{
			  extend: 'colvis',
			  text: 'Colunas',
			   exportOptions: {
				columns: ':not(:last-child)',
			   }
			}
        ],
	});




});





