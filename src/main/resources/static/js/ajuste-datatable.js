$(document).ready(function () {
	moment.locale('pt-br');
	var table = $("#table-server").DataTable({
		processing: true,
		serverSide: true,
		responsive: true,
		info:true,
		lengthChange:true,
		lengthMenu: [15, 40, 60, -1],
		"order": [8, "asc"],
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
			url: "/serverAjustes",
			data: "data",
		},
		columns: [
			{ data: 'id' },
			{ data: 'locacao' },
			{ data: 'produto' },
			{ data: 'tipo' },
			{ data: 'cor' },
			{ data: 'tamanho' },
			{ data: 'cliente' },
			//{data: 'status'} ,
			{
				data: 'status',
				render: function (data, type, row, meta) {
					if (data == 'Aberto') {
						return type === 'display'
							? ' <a id="btn-alert1" class="btn badge badge-success"  role="button">Liberado</a>'
							: data;
					}
					else {
						return type === 'display'
							? ' <a id="btn-alert1" class="btn badge badge-danger" href="/liberarProdutoNew/' + data + '" role="button">Pendente</a>'
							: data;
					}


				}
			},
			{
				data: 'liberacao', render:
					function (dtCadastro) {
						return moment(dtCadastro).format('L');
					},
			},
			{
				data: 'dias',

				render: function (data, type, row, meta) {
					return type === 'display'
						? '<progress value="' + 1 + '" max="' + data + '"></progress>'
						: data;
				}
			},

			{
				data: 'foto',
				render: function (data) {
					return '<img src="data:image/jpeg;base64,' + data + '" width="40px">';
				}
			}
		],
		dom: 'Bfrtip',
		buttons: [
			{
				text: 'Detalhes',
				color: "#f8f9fa",
				attr: {
					id: 'btn-editar',
					type: 'button'
				},
				enabled: false
			},
			{
				text: 'Baixar',
				attr: {
					id: 'btn-excluir',
					type: 'button'
				},
				enabled: false
			}
		]
	});





	// acao para marcar/desmarcar botoes ao clicar na ordenacao 
	$("#table-server thead").on('click', 'tr', function () {
		table.buttons().disable();
	});

	// acao para marcar/desmarcar linhas clicadas 
	$("#table-server tbody").on('click', 'tr', function () {
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
	$("#btn-editar").on('click', function () {
		if (isSelectedRow()) {

			var id = getPromoId();
			$.ajax({
				method: "GET",
				url: "/editarmodalajuste/" + id,

				beforeSend: function () {
					// removendo as mensagens
					$("span").closest('.error-span').remove();
					//remover as bordas vermelhas
					$(".is-invalid").removeClass("is-invalid");
					// abre o modal
					$("#modal-form").modal('show');
				},
				success: function (data) {
					$("#edt_id").val(data.id);
					$("#edt_idLocacao").val(data.locacao);
					$("#edt_cliente").val(data.cliente);
					$("#edt_produto").val(data.produto);
					$("#edt_tipo").val(data.tipo);
					$("#edt_cor").val(data.cor);
					$("#edt_tamanho").val(data.tamanho);
					$("#edt_ajuste").val(data.ajuste);
					$("#edt_foto").val('<img src="data:image/jpeg;base64,' + data.foto + '" width="40px">');
					$("#edt_cidade").val(data.cidade);
					$("#edt_telefone").val(data.telefone);
					$("#edt_atendente").val(data.atendente);
					$("#edt_outros").val(data.outros);
					var image = $("<img>", {
						"src": "data:image/png;base64," + data.foto,
						"width": "350px",
						"height": "350px"
					});
					var row = $('<tr></tr>').append('<td></td>').html(image);
					$("#edt_foto").html(row);
				},
				error: function () {
					alert("Ops... algum erro ocorreu, tente novamente.");
				}
			});
		}
	});


	// submit do formulario para editar
	$("#btn-edit-modal").on("click", function () {
		var promo = {};
		promo.ajuste = $("#edt_ajuste").val();
		promo.id = $("#edt_id").val();

		$.ajax({
			method: "POST",
			url: "/editarmodalajuste",
			data: promo,
			beforeSend: function () {
				// removendo as mensagens
				$("span").closest('.error-span').remove();
				//remover as bordas vermelhas
				$(".is-invalid").removeClass("is-invalid");
			},
			success: function () {
				$("#modal-form").modal("hide");
				table.buttons().disable();
				table.ajax.reload();
			},
			statusCode: {
				422: function (xhr) {
					console.log('status error:', xhr.status);
					var errors = $.parseJSON(xhr.responseText);
					$.each(errors, function (key, val) {
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
	$("#edt_linkImagem").on("change", function () {
		var link = $(this).val();
		$("#edt_imagem").attr("src", link);
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

	function getPromoId() {
		return table.row(table.$('tr.selected')).data().id;
	}

	function isSelectedRow() {
		var trow = table.row(table.$('tr.selected'));
		return trow.data() !== undefined;
	}
});


// SSE
window.onload = init();
var totalOfertas = new Number(0);
function init() {

	const evtSource = new EventSource("/promocao/notificacao");

	evtSource.onopen = (event) => {
		//  console.log("The connection has been established.");
	};

	evtSource.onmessage = (event) => {
		const count = event.data;
		if (count > 0) showButton(count);
	};
}

function showButton(count) {
	totalOfertas = totalOfertas + new Number(count);
	$("#btn-alert1").show(function () {
		$(this)
			.attr("style", "display: block;")
			.text("Veja " + totalOfertas + " novo(s) Produtos(s)!");
	});
}






