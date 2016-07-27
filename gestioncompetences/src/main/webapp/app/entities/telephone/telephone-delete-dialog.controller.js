(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('TelephoneDeleteController',TelephoneDeleteController);

    TelephoneDeleteController.$inject = ['$uibModalInstance', 'entity', 'Telephone'];

    function TelephoneDeleteController($uibModalInstance, entity, Telephone) {
        var vm = this;

        vm.telephone = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Telephone.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
