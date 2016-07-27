(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('PublicationDetailController', PublicationDetailController);

    PublicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Publication', 'Collaborateur'];

    function PublicationDetailController($scope, $rootScope, $stateParams, entity, Publication, Collaborateur) {
        var vm = this;

        vm.publication = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:publicationUpdate', function(event, result) {
            vm.publication = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
