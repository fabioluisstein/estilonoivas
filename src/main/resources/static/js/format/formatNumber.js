$( document ).ready(function() {
$("#valor").maskMoney({ allowNegative: true, thousands:'.', decimal:',', affixesStay: false});

const formatter = new Intl.NumberFormat('pt-BR', {
    currency : 'BRL',
    prefix:'R$ ',
    minimumFractionDigits : 2
});
$("#valor").val(formatter.format($("#valor").val()));
$("#valor").focus();
$("#valor").maskMoney({showSymbol:true,  decimal:",", thousands:"."});




$("#valorCompra").maskMoney({ allowNegative: true, thousands:'.', decimal:',', affixesStay: false});
$("#valorCompra").val(formatter.format($("#valorCompra").val()));
$("#valorCompra").focus();
$("#valorCompra").maskMoney({showSymbol:true,  decimal:",", thousands:"."});


$("#valorVenda").maskMoney({ allowNegative: true, thousands:'.', decimal:',', affixesStay: false});
$("#valorVenda").val(formatter.format($("#valorVenda").val()));
$("#valorVenda").focus();
$("#valorVenda").maskMoney({showSymbol:true,  decimal:",", thousands:"."});


});
