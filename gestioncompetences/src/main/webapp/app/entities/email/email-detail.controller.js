(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('EmailDetailController', EmailDetailController);

    EmailDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Email', 'Collaborateur'];

    function EmailDetailController($scope, $rootScope, $stateParams, entity, Email, Collaborateur) {
        var vm = this;

        vm.email = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:emailUpdate', function(event, result) {
            vm.email = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
