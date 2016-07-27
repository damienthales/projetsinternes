(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('RubriqueDialogController', RubriqueDialogController);

    RubriqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Rubrique'];

    function RubriqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Rubrique) {
        var vm = this;

        vm.rubrique = entity;
        vm.clear = clear;
        vm.save = save;
        vm.rubriques = Rubrique.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rubrique.id !== null) {
                Rubrique.update(vm.rubrique, onSaveSuccess, onSaveError);
            } else {
                Rubrique.save(vm.rubrique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:rubriqueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
