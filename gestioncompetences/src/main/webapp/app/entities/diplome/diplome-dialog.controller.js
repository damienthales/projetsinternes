(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DiplomeDialogController', DiplomeDialogController);

    DiplomeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Diplome', 'Collaborateur'];

    function DiplomeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Diplome, Collaborateur) {
        var vm = this;

        vm.diplome = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.diplome.id !== null) {
                Diplome.update(vm.diplome, onSaveSuccess, onSaveError);
            } else {
                Diplome.save(vm.diplome, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:diplomeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.diplomeDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
