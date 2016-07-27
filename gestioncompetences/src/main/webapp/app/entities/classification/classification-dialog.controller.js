(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('ClassificationDialogController', ClassificationDialogController);

    ClassificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Classification', 'Collaborateur'];

    function ClassificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Classification, Collaborateur) {
        var vm = this;

        vm.classification = entity;
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
            if (vm.classification.id !== null) {
                Classification.update(vm.classification, onSaveSuccess, onSaveError);
            } else {
                Classification.save(vm.classification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:classificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.classificationDateDebut = false;
        vm.datePickerOpenStatus.classificationDateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
