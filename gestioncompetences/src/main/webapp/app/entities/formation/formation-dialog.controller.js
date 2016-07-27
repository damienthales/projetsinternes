(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('FormationDialogController', FormationDialogController);

    FormationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Formation', 'Collaborateur'];

    function FormationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Formation, Collaborateur) {
        var vm = this;

        vm.formation = entity;
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
            if (vm.formation.id !== null) {
                Formation.update(vm.formation, onSaveSuccess, onSaveError);
            } else {
                Formation.save(vm.formation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:formationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.formationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
