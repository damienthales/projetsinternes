(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DiplomeDetailController', DiplomeDetailController);

    DiplomeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Diplome', 'Collaborateur'];

    function DiplomeDetailController($scope, $rootScope, $stateParams, entity, Diplome, Collaborateur) {
        var vm = this;

        vm.diplome = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:diplomeUpdate', function(event, result) {
            vm.diplome = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
