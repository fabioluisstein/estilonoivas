var Financeiro = Financeiro || {};

Financeiro.Autocomplete = (function () {

    function Autocomplete () {

    }

    Autocomplete.prototype.iniciar = function () {
    	 
        $.ajax({
        
        	   type: 'GET',
               url:  '/filtro?nome=',
            
          success: function (response) {
              var entidadesServidor = response;
              var entidadesSugestao = {};

              for (var i = 0; i < entidadesServidor.length; i++){
                    entidadesSugestao[entidadesServidor[i].nome] = entidadesServidor[i].flag;
              }

              $('input.autocomplete').autocomplete({
                  data: entidadesSugestao,
                  limit: 6,
                  onAutocomplete: function(texto) {
                      onSendItem(texto);
                  },
                  minLength: 2
              });

              function onSendItem(texto) {
                  var id = 0;
                  entidadesServidor.filter(function (obj) {
                      if(obj.nome === texto){
                        id = obj.id;
                     
                     
                      }

                  });

                  if(id > 0){
                	//  const nome = document.querySelector("#autocomplete-input-estado");
                	  
                //	console.log(nome.value);
                      $('#estadoId').attr('value', id);
                  }
                  
                  
        
                  
                  
              }

          }

      });
    };

    return Autocomplete;

}());

$(function () {

    var autocomplete = new Financeiro.Autocomplete();
    autocomplete.iniciar();


}  );