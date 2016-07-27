(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CollaborateurDeleteController',CollaborateurDeleteController);

    CollaborateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Collaborateur'];

    function CollaborateurDeleteController($uibModalInstance, entity, Collaborateur) {
        var vm = this;

        vm.collaborateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Collaborateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
