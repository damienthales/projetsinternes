(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CompetenceDetailController', CompetenceDetailController);

    CompetenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Competence', 'Collaborateur'];

    function CompetenceDetailController($scope, $rootScope, $stateParams, entity, Competence, Collaborateur) {
        var vm = this;

        vm.competence = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:competenceUpdate', function(event, result) {
            vm.competence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
