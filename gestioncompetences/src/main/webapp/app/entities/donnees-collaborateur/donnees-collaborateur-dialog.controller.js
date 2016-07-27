(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesCollaborateurDialogController', DonneesCollaborateurDialogController);

    DonneesCollaborateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'DonneesCollaborateur', 'Rubrique'];

    function DonneesCollaborateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, DonneesCollaborateur, Rubrique) {
        var vm = this;

        vm.donneesCollaborateur = entity;
        vm.clear = clear;
        vm.save = save;
        vm.rubriques = Rubrique.query({filter: 'donneescollaborateur-is-null'});
        $q.all([vm.donneesCollaborateur.$promise, vm.rubriques.$promise]).then(function() {
            if (!vm.donneesCollaborateur.rubrique || !vm.donneesCollaborateur.rubrique.id) {
                return $q.reject();
            }
            return Rubrique.get({id : vm.donneesCollaborateur.rubrique.id}).$promise;
        }).then(function(rubrique) {
            vm.rubriques.push(rubrique);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.donneesCollaborateur.id !== null) {
                DonneesCollaborateur.update(vm.donneesCollaborateur, onSaveSuccess, onSaveError);
            } else {
                DonneesCollaborateur.save(vm.donneesCollaborateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:donneesCollaborateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
