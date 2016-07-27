(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesRubriqueDeleteController',DonneesRubriqueDeleteController);

    DonneesRubriqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'DonneesRubrique'];

    function DonneesRubriqueDeleteController($uibModalInstance, entity, DonneesRubrique) {
        var vm = this;

        vm.donneesRubrique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DonneesRubrique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
