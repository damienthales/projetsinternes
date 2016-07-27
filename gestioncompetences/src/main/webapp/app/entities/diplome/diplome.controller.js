(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DiplomeController', DiplomeController);

    DiplomeController.$inject = ['$scope', '$state', 'Diplome'];

    function DiplomeController ($scope, $state, Diplome) {
        var vm = this;
        
        vm.diplomes = [];

        loadAll();

        function loadAll() {
            Diplome.query(function(result) {
                vm.diplomes = result;
            });
        }
    }
})();
