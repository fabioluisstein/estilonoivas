$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-contas-server").DataTable({
		processing: true,
		serverSide: true,
		responsive: true,
		info:true,
		lengthChange:false,
		lengthMenu: [15, 40, 60, -1],
		"order": [0, "asc"],
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
		},
		  columns: [	
			{ "data": 'id',
			   render: function ( data, type, row) {
			    return '<a href="/editarconta/'+row.id+'">'+row.id+'</a>';
			   }
		    },
			{ "data": 'instituicao',
			   render: function ( data, type, row) {
			    return '<a href="/editarconta/'+row.id+'">'+row.instituicao+'</a>';
			  } 
		    },
			{ "data": 'tipo',
			   render: function ( data, type, row) {
			    return '<a href="/editarconta/'+row.id+'">'+row.tipo+'</a>';
			   }
		    },
			{ "data": 'valor',
			   render: function ( valor, type, row) {	
			    return  '<a href="/editarconta/'+row.id+'">'+ valor.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'})+'</a>';
		      }
	      },

			{ "data": 'data',
			   render: function ( data, type, row) {	
			    return  '<a href="/editarconta/'+row.id+'">'+moment(row.data).format('L')+'</a>';
		     }
		    },
			{ "data": 'id',
			   render: function ( data, type, row) {	
			    return  '<a href="/removerconta/' + row.id + '" class="btn btn-danger"><i class="fas fa-trash">';
		       }
		    }
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





