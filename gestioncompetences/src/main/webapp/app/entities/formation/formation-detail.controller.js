(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('FormationDetailController', FormationDetailController);

    FormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Formation', 'Collaborateur'];

    function FormationDetailController($scope, $rootScope, $stateParams, entity, Formation, Collaborateur) {
        var vm = this;

        vm.formation = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:formationUpdate', function(event, result) {
            vm.formation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
