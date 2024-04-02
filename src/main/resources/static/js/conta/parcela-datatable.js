$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-parcelas-server").DataTable({
		processing: true,
		serverSide: true,
		responsive: true,
		info:true,
		lengthChange:false,
		lengthMenu: [15, 40, 60, -1],
		"order": [8, "desc"],
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
			{ "data": 'stparcela',
			"width": '3%',
			render: function ( data, type, row) {	 
				if (row.stparcela == "problema") { 
				
					return  '<td > <i class="fas fa-exclamation-triangle"></i>    </td>';
			}


			if (row.stparcela == "banco") { 
				
				return  '<td > <i class="fas fa-landmark"></i> </td>';
		} 
			
				else{
					return  '<td > <i class="fas fa-thumbs-up"></i>    </td>';				
				}
			}
		},	
            { "data": 'id'} ,
			{ "data": 'locacao'} ,
			{ "data": 'cliente'},              
		    { "data": 'cpf'},
			{ "data": 'cidade'},
		    { "data": 'nf'},
	        { "data": 'observacao'},
		    { data: 'pagamento', render:
				function (pagamento) {
					return moment(pagamento).format('L');
				},
		    },
		    {
			  data: 'vencimento', render:
			   function (vencimento) {
					return moment(vencimento).format('L');
			   },
		      },
            { "data": 'moeda' },
		    { "data": 'valor' ,
			   render: function ( valor, type, row) {	
			    return  valor.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'});
		       }
	        },
			{ "data": 'atendente'},
			{ "data": 'banco'},
			{ "data": 'id',
			 "width": '3%',
			render: function ( data, type, row) {	 

				if (row.arquivo  !== null &&  row.arquivo  !== '') { 
					
					return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="/editarParcelaCustom/'+row.id+'" class="btn btn-info"><i class="fas fa-edit"></i></a> <div> </div>  <button onclick="if (confirm(\'Deseja baixar o arquivo?\')) { window.location.href = \'/baixarArquivoParcela/' + row.id +'\'; }  else {  }" class="btn btn-danger"><i class="fas fa-download"></i></button> </div> </td>';
				
				}
				else{
					return  '<td class="text-right py-0 align-middle"> <div class="btn-group btn-group-sm"> <a href="/editarParcelaCustom/'+row.id+'" class="btn btn-info"><i class="fas fa-edit"></i></a> </div> </td>';
				}
			 
			
			}
		 }
		],

		columnDefs: [
			{ targets: [ 0, 14 ], orderable: false },	
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





