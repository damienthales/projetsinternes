(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('FonctionDialogController', FonctionDialogController);

    FonctionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fonction', 'Collaborateur'];

    function FonctionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fonction, Collaborateur) {
        var vm = this;

        vm.fonction = entity;
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
            if (vm.fonction.id !== null) {
                Fonction.update(vm.fonction, onSaveSuccess, onSaveError);
            } else {
                Fonction.save(vm.fonction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:fonctionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fonctionDateDebut = false;
        vm.datePickerOpenStatus.fonctionDateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
