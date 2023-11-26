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

 


});