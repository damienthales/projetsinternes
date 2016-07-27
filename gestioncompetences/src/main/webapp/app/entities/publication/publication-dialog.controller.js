(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('PublicationDialogController', PublicationDialogController);

    PublicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Publication', 'Collaborateur'];

    function PublicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Publication, Collaborateur) {
        var vm = this;

        vm.publication = entity;
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
            if (vm.publication.id !== null) {
                Publication.update(vm.publication, onSaveSuccess, onSaveError);
            } else {
                Publication.save(vm.publication, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:publicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publicationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
