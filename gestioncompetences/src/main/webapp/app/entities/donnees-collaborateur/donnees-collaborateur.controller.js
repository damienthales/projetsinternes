(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesCollaborateurController', DonneesCollaborateurController);

    DonneesCollaborateurController.$inject = ['$scope', '$state', 'DonneesCollaborateur'];

    function DonneesCollaborateurController ($scope, $state, DonneesCollaborateur) {
        var vm = this;
        
        vm.donneesCollaborateurs = [];

        loadAll();

        function loadAll() {
            DonneesCollaborateur.query(function(result) {
                vm.donneesCollaborateurs = result;
            });
        }
    }
})();
