(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesCollaborateurDeleteController',DonneesCollaborateurDeleteController);

    DonneesCollaborateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'DonneesCollaborateur'];

    function DonneesCollaborateurDeleteController($uibModalInstance, entity, DonneesCollaborateur) {
        var vm = this;

        vm.donneesCollaborateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DonneesCollaborateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
