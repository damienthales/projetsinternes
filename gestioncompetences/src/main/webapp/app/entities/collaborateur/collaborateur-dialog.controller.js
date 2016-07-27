(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CollaborateurDialogController', CollaborateurDialogController);

    CollaborateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Collaborateur', 'Email', 'Adresse', 'Fonction', 'Classification', 'Formation', 'Publication', 'Diplome', 'Cv'];

    function CollaborateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Collaborateur, Email, Adresse, Fonction, Classification, Formation, Publication, Diplome, Cv) {
        var vm = this;

        vm.collaborateur = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.emails = Email.query();
        vm.adresses = Adresse.query();
        vm.fonctions = Fonction.query();
        vm.classifications = Classification.query();
        vm.formations = Formation.query();
        vm.publications = Publication.query();
        vm.diplomes = Diplome.query();
        vm.cvs = Cv.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.collaborateur.id !== null) {
                Collaborateur.update(vm.collaborateur, onSaveSuccess, onSaveError);
            } else {
                Collaborateur.save(vm.collaborateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:collaborateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.collaborateurDateNaissance = false;
        vm.datePickerOpenStatus.collaborateurDateArrivee = false;

        vm.setCollaborateurPhotos = function ($file, collaborateur) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        collaborateur.collaborateurPhotos = base64Data;
                        collaborateur.collaborateurPhotosContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
