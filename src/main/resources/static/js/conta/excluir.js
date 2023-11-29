$(document).ready(function myFunction() {
    var itemIdToDelete;

    // Captura o ID do item ao clicar no botão de exclusão
    $('.btn-danger').click(function() {
      
        //itemIdToDelete = $(this).data('modal-delete:item-id');

        var minhaVar = $('#modal-delete').modal();

        const inputData = document.getElementById('inputData').value;

        const meuModal = document.getElementById('modal-delete');
        const valorDataVar = meuModal.getAttribute('id');

     
        console.log(meuModal); // Saída: algum-valor
        console.log(inputData); // Saída: algum-valor
        
       


    });
    

    // Ao confirmar a exclusão no modal, envia a requisição DELETE
    $('#confirmDelete').click(function() {
        if (itemIdToDelete) {
            $.ajax({
                type: 'DELETE',
                url: '/seu-endpoint-exclusao/' + itemIdToDelete,
                success: function(response) {
                    // Lógica após a exclusão (recarregar página, atualizar lista, etc.)
                    window.location.reload();
                },
                error: function(err) {
                    console.error('Erro ao excluir item:', err);
                }
            });
        }
    });









});