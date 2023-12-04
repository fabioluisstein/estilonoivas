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


const input = document.getElementById("cpf");
input.addEventListener("keyup", formatarCPF);

function formatarCPF(e){

var v=e.target.value.replace(/\D/g,"");
v=v.replace(/(\d{3})(\d)/,"$1.$2");
v=v.replace(/(\d{3})(\d)/,"$1.$2");
v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");
e.target.value = v;
} ;


});





/* Máscaras de Telefones */
function mascara(o,f){
    v_obj=o
    v_fun=f
    setTimeout("execmascara()",1)
}
function execmascara(){
    v_obj.value=v_fun(v_obj.value)
}
function mtel(v){
    v=v.replace(/\D/g,""); //Remove tudo o que não é dígito
    v=v.replace(/^(\d{2})(\d)/g,"($1) $2"); //Coloca parênteses em volta dos dois primeiros dígitos
    v=v.replace(/(\d)(\d{4})$/,"$1-$2"); //Coloca hífen entre o quarto e o quinto dígitos
    return v;
}
function id( el ){
	return document.getElementById( el );
}
window.onload = function(){
	id('telefone').onkeyup = function(){
		mascara( this, mtel );
	}
}
