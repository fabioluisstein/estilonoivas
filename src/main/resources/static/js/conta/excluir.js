$(document).ready(function() {
    // Exibir modal ao clicar no botão de exclusão
    $('.delete-btn').click(function() {
        var itemId = $(this).data('item-id');
        $('.modal').fadeIn();
        // Ao clicar em 'Sim', chamar a função de exclusão
        $('.confirm-delete').click(function() {
            excluirItem(itemId);
        });
        // Ao clicar em 'Cancelar', fechar o modal
        $('.cancel-delete').click(function() {
            $('.modal').fadeOut();
        });
    });

    // Função para excluir o item (pode ser chamada via Ajax para o backend)
    function excluirItem(itemId) {
        // Chamada Ajax para o backend (Spring Boot) para excluir o item com o ID especificado
        $.ajax({
            type: 'DELETE',
            url: '/seu-endpoint-exclusao/' + itemId,
            success: function(response) {
                // Lógica após a exclusão (por exemplo, recarregar a página ou atualizar a lista de itens)
                window.location.reload();
            },
            error: function(err) {
                console.error('Erro ao excluir item:', err);
            }
        });
    }
});