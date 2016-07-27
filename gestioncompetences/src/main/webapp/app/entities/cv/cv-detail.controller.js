(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CvDetailController', CvDetailController);

    CvDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cv', 'DonneesRubrique', 'Collaborateur'];

    function CvDetailController($scope, $rootScope, $stateParams, entity, Cv, DonneesRubrique, Collaborateur) {
        var vm = this;

        vm.cv = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:cvUpdate', function(event, result) {
            vm.cv = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
