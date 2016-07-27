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
        
        vm.predicateAdresse = 'id';
        vm.resetAdresse = resetAdresse;
        vm.reverseAdresse = true;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:collaborateurUpdate', function(event, result) {
            vm.collaborateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        
        sort();
        
        function sort() {
            var result = [vm.predicateAdresse + ',' + (vm.reverseAdresse ? 'asc' : 'desc')];
            if (vm.predicateAdresse !== 'id') {
                result.push('id');
            }
            return result;
        }
        function resetAdresse() {
            vm.page = 0;
            vm.collaborateurs = [];
        }
    }
})();
