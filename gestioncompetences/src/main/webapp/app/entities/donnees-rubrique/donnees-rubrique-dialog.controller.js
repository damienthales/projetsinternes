(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesRubriqueDialogController', DonneesRubriqueDialogController);

    DonneesRubriqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DonneesRubrique', 'Cv', 'Rubrique'];

    function DonneesRubriqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DonneesRubrique, Cv, Rubrique) {
        var vm = this;

        vm.donneesRubrique = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cvs = Cv.query();
        vm.rubriques = Rubrique.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.donneesRubrique.id !== null) {
                DonneesRubrique.update(vm.donneesRubrique, onSaveSuccess, onSaveError);
            } else {
                DonneesRubrique.save(vm.donneesRubrique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:donneesRubriqueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.donneesRubriqueDateDebut = false;
        vm.datePickerOpenStatus.donneesRubriqueDateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
