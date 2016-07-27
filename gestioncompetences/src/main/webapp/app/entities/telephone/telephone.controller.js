(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('TelephoneController', TelephoneController);

    TelephoneController.$inject = ['$scope', '$state', 'Telephone'];

    function TelephoneController ($scope, $state, Telephone) {
        var vm = this;
        
        vm.telephones = [];

        loadAll();

        function loadAll() {
            Telephone.query(function(result) {
                vm.telephones = result;
            });
        }
    }
})();
