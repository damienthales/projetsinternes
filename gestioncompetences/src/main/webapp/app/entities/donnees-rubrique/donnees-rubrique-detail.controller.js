(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesRubriqueDetailController', DonneesRubriqueDetailController);

    DonneesRubriqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DonneesRubrique', 'Cv', 'Rubrique'];

    function DonneesRubriqueDetailController($scope, $rootScope, $stateParams, entity, DonneesRubrique, Cv, Rubrique) {
        var vm = this;

        vm.donneesRubrique = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:donneesRubriqueUpdate', function(event, result) {
            vm.donneesRubrique = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
