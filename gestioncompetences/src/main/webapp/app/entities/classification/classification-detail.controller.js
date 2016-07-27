(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('ClassificationDetailController', ClassificationDetailController);

    ClassificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Classification', 'Collaborateur'];

    function ClassificationDetailController($scope, $rootScope, $stateParams, entity, Classification, Collaborateur) {
        var vm = this;

        vm.classification = entity;

        var unsubscribe = $rootScope.$on('gestioncompetencesApp:classificationUpdate', function(event, result) {
            vm.classification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
