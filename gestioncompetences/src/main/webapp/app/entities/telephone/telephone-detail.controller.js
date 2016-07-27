(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('TelephoneDetailController', TelephoneDetailController);

    TelephoneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Telephone'];

    function TelephoneDetailController($scope, $rootScope, $stateParams, entity, Telephone) {
        var vm = this;

        vm.telephone = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:telephoneUpdate', function(event, result) {
            vm.telephone = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
