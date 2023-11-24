$(document).ready(function(){
	moment.locale('pt-br');
	var table = $("#table-server").DataTable({
		processing: true,
		serverSide: true,
		responsive: true,
		scrollY:true,
		lengthMenu: [ 10, 15, 20, 50 ],
		ajax: {
			url: "/promocao/datatables/server",
			data: "data"
		},
		columns: [
	
			{data: 'id'},
			{data: 'nome'},
			{data: 'telefone'},
			{
				data: 'telefone',
				   render: function (data, type) {
							   link = 'https://wa.me/55';
						   return '<a href="' + link + '">' + data + '</a>';   
				   }
				   },
			{data: 'cpf'},
		{data: 'cidade'}
	],
	dom: 'Bfrtip',
	buttons: [
		{
			text: 'Editar',
			attr: {
				id: 'btn-editar',
				type: 'button'					
			},
			enabled: false
		},
		{
			text: 'Excluir',
			attr: {
				id: 'btn-excluir',
				type: 'button'
			},
			enabled: false
		}
	]
});
	
	// acao para marcar/desmarcar botoes ao clicar na ordenacao 
	$("#table-server thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
	
	// acao para marcar/desmarcar linhas clicadas 
	$("#table-server tbody").on('click', 'tr', function() {		
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');	
			table.buttons().disable();
		} else {			
			$('tr.selected').removeClass('selected');			
			$(this).addClass('selected');
			table.buttons().enable();
		}
	});
	
	// acao do botao de editar (abrir modal)
	$("#btn-editar").on('click', function() {		
		if ( isSelectedRow() ) {	
			
			var id = getPromoId();
			$.ajax({
				method: "GET",
				url: "/promocao/edit/" + id,
				beforeSend: function() {
					// removendo as mensagens
					$("span").closest('.error-span').remove();				
					//remover as bordas vermelhas
					$(".is-invalid").removeClass("is-invalid");
					// abre o modal
					$("#modal-form").modal('show');
				},
				success: function( data ) {
					$("#edt_id").val(data.id);
					$("#edt_nome").text(data.nome);
				
				},
				error: function() {
					alert("Ops... algum erro ocorreu, tente novamente.");
				}
			});			
		}
	});
	
	// submit do formulario para editar
	$("#btn-edit-modal").on("click", function() {
		var promo = {};
		promo.descricao = $("#edt_descricao").val();
		promo.preco = $("#edt_preco").val();
		promo.titulo = $("#edt_titulo").val();
		promo.categoria = $("#edt_categoria").val();
		promo.linkImagem = $("#edt_linkImagem").val();
		promo.nome = $("#edt_nome").val();
		promo.id = $("#edt_id").val();
		
		$.ajax({
			method: "POST",
			url: "/promocao/edit",
			data: promo,
			beforeSend: function() {
				// removendo as mensagens
				$("span").closest('.error-span').remove();				
				//remover as bordas vermelhas
				$(".is-invalid").removeClass("is-invalid");
			},
			success: function() {
				$("#modal-form").modal("hide");
				table.buttons().disable();
				table.ajax.reload();
			},
			statusCode: {
				422: function(xhr) {
					console.log('status error:', xhr.status);
					var errors = $.parseJSON(xhr.responseText);
					$.each(errors, function(key, val){
						$("#edt_" + key).addClass("is-invalid");
						$("#error-" + key)
							.addClass("invalid-feedback")
							.append("<span class='error-span'>" + val + "</span>")
					});
				}
			}
		});
	});
	
	// alterar a imagem no componente <img> do modal
	$("#edt_linkImagem").on("change", function() {
		var link = $(this).val();
		$("#edt_imagem").attr("src", link);
	});
	
	// acao do botao de excluir (abrir modal)
	$("#btn-excluir").on('click', function() {
		if ( isSelectedRow() ) {
			$("#modal-delete").modal('show');
		}
	});
	
	// exclusao de uma promocao
	$("#btn-del-modal").on('click', function() {
		var id = getPromoId();
		$.ajax({
			method: "GET",
			url: "/promocao/delete/" + id,
			success: function() {
				$("#modal-delete").modal('hide');
				table.buttons().disable();
				table.ajax.reload();
			},
			error: function() {
				alert("Ops... Ocorreu um erro, tente mais tarde.");
			}
		});
	});
	
	function getPromoId() {
		return table.row(table.$('tr.selected')).data().id;
	}
	
	function isSelectedRow() {
		var trow = table.row(table.$('tr.selected'));
		return trow.data() !== undefined;
	}
	
	
	
	
	
});