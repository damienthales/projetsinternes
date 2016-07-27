(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('FonctionDetailController', FonctionDetailController);

    FonctionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fonction', 'Collaborateur'];

    function FonctionDetailController($scope, $rootScope, $stateParams, entity, Fonction, Collaborateur) {
        var vm = this;

        vm.fonction = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:fonctionUpdate', function(event, result) {
            vm.fonction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
