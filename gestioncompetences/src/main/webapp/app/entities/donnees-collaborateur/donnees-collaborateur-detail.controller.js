(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('DonneesCollaborateurDetailController', DonneesCollaborateurDetailController);

    DonneesCollaborateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DonneesCollaborateur', 'Rubrique'];

    function DonneesCollaborateurDetailController($scope, $rootScope, $stateParams, entity, DonneesCollaborateur, Rubrique) {
        var vm = this;

        vm.donneesCollaborateur = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:donneesCollaborateurUpdate', function(event, result) {
            vm.donneesCollaborateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
