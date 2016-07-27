(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CvDialogController', CvDialogController);

    CvDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cv', 'DonneesRubrique', 'Collaborateur'];

    function CvDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cv, DonneesRubrique, Collaborateur) {
        var vm = this;

        vm.cv = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.donneesrubriques = DonneesRubrique.query();
        vm.collaborateurs = Collaborateur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cv.id !== null) {
                Cv.update(vm.cv, onSaveSuccess, onSaveError);
            } else {
                Cv.save(vm.cv, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:cvUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCv = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
