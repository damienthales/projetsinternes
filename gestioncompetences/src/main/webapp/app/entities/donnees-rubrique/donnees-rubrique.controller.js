(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesRubriqueController', DonneesRubriqueController);

    DonneesRubriqueController.$inject = ['$scope', '$state', 'DonneesRubrique'];

    function DonneesRubriqueController ($scope, $state, DonneesRubrique) {
        var vm = this;
        
        vm.donneesRubriques = [];

        loadAll();

        function loadAll() {
            DonneesRubrique.query(function(result) {
                vm.donneesRubriques = result;
            });
        }
    }
})();
