(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('TelephoneDialogController', TelephoneDialogController);

    TelephoneDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Telephone'];

    function TelephoneDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Telephone) {
        var vm = this;

        vm.telephone = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.telephone.id !== null) {
                Telephone.update(vm.telephone, onSaveSuccess, onSaveError);
            } else {
                Telephone.save(vm.telephone, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:telephoneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
