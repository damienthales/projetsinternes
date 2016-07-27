(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('AdresseDetailController', AdresseDetailController);

    AdresseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Adresse', 'Collaborateur'];

    function AdresseDetailController($scope, $rootScope, $stateParams, entity, Adresse, Collaborateur) {
        var vm = this;

        vm.adresse = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:adresseUpdate', function(event, result) {
            vm.adresse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
