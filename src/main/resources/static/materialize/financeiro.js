var Financeiro = Financeiro || {};

Financeiro.DialogoDeRemocao = (function () {

    function DialogoDeRemocao() {
        this.modal = $('#modal-remover-entidade');
        this.botaoRemover = $('.js-remover-entidade-btn');
        this.alertInfo = $('#info');
        this.alertErro = $('#erro');
    }

    DialogoDeRemocao.prototype.iniciar = function () {
      this.botaoRemover.on('click', onModalShow.bind(this));
      this.alertInfo.on('dblclick', onDoubleClickInfo);
        this.alertErro.on('dblclick', onDoubleClickInfo);
    };


    function onDoubleClickInfo(evento) {
        $(this).remove();
    }

    return DialogoDeRemocao;

}());

$(function () {

    var removerEntidade = new Financeiro.DialogoDeRemocao();
    removerEntidade.iniciar();
});