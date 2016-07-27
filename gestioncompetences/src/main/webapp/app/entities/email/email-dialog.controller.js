(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('EmailDialogController', EmailDialogController);

    EmailDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Email', 'Collaborateur'];

    function EmailDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Email, Collaborateur) {
        var vm = this;

        vm.email = entity;
        vm.clear = clear;
        vm.save = save;
        vm.collaborateurs = Collaborateur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.email.id !== null) {
                Email.update(vm.email, onSaveSuccess, onSaveError);
            } else {
                Email.save(vm.email, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:emailUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
