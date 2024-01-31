$(document).ready(function () {
	// Configurar evento de exibição do modal
	$('#detalhesModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget); // Botão que acionou o modal
		var produtoId = button.data('id'); // Extrair informações dos atributos data-*
		// Fazer uma chamada AJAX para obter detalhes do produto por ID
		$.ajax({
			type: 'GET',
			url: '/detalhesProduto/' + produtoId,
			success: function (produto) {
				// Preencher os campos do modal com os detalhes do produto
				
				$('#id_locacao_produto').val(produto.id);
				$('#observacaoProduto').text(produto.observacao);
				$('#produto').text(produto.produto);
				
				$('#valorProduto').val(produto.valor);
				$('#data_liberacao').val(produto.data_liberacao);
				$('#idlocacaoProduto').val(produto.locacao);
				
				console.log(produto);
				
				
			  
			},
			error: function () {
				console.log('Erro ao obter detalhes do produto');
			}
		});
	});
});


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

			$("#valorProduto").val(response.valorVenda).maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
			

			const formatter = new Intl.NumberFormat('pt-BR', {
			    currency : 'BRL',
			    minimumFractionDigits : 2
			});
			
			$("#valorProduto").val(formatter.format($("#valorProduto").val()));
			$("#valorProduto").focus();
			$("#valorProduto").maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
		
			}
		}).fail(function(xhr, status, errorThrown) {
			alert("Erro ao buscar produto por id: " + xhr.responseText);
		});
	  
}
	
	





