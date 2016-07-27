(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CvDeleteController',CvDeleteController);

    CvDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cv'];

    function CvDeleteController($uibModalInstance, entity, Cv) {
        var vm = this;

        vm.cv = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cv.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
