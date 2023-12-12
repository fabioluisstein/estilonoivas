$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-parcelas-server").DataTable({
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
			url: "/serverParcelas",
			data: "data",
			 error : function(e) {
				window.location.href = "/listaParcelas";
			 }

			
		},
		  columns: [	
            { "data": 'id'} ,
			{ "data": 'cliente'},              
		    { "data": 'cpf'},
			{ "data": 'cidade'},
		    { "data": 'nf'},
	        { "data": 'observacao'},
			{ "data": 'vencimento',
			  render: function ( data, type, row) {	
			  return  moment(row.data).format('L');
		    }
		    },
			{ "data": 'pagamento',
			  render: function ( data, type, row) {	
			   return  moment(row.data).format('L');
		      }
		    },
            { "data": 'moeda' },
		    { "data": 'valor' ,
			   render: function ( valor, type, row) {	
			    return  valor.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'});
		       }
	        },
			{ "data": 'atendente'},
			{ "data": 'arquivo',
			"width": '3%',
			render: function ( data, type, row) {	 
				if (row.arquivo === null) { 
					return  '<td class="text-right py-0 align-middle"> </td>';
			}
				else{
				return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="baixarArquivoParcela/'+row.id+'" class="btn btn-info"><i class="fas fa-download"></i></a> </div>    </td>';
				}
			}
		},
			{ "data": 'id',
			"width": '3%',
			render: function ( data, type, row) {	 
			 return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="/editarParcelaCustom/'+row.id+'" class="btn btn-info"><i class="fas fa-edit"></i></a> </div>  </td>';
			}
		 }
		],

		columnDefs: [
			{ targets: [ 11, 12 ], orderable: false },	
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





