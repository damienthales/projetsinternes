(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CollaborateurDetailController', CollaborateurDetailController);

    CollaborateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Collaborateur', 'Email', 'Adresse', 'Fonction', 'Classification', 'Formation', 'Publication', 'Diplome', 'Cv'];

    function CollaborateurDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Collaborateur, Email, Adresse, Fonction, Classification, Formation, Publication, Diplome, Cv) {
        var vm = this;

        vm.collaborateur = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:collaborateurUpdate', function(event, result) {
            vm.collaborateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
