(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('RubriqueDeleteController',RubriqueDeleteController);

    RubriqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rubrique'];

    function RubriqueDeleteController($uibModalInstance, entity, Rubrique) {
        var vm = this;

        vm.rubrique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Rubrique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
