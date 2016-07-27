(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CompetenceController', CompetenceController);

    CompetenceController.$inject = ['$scope', '$state', 'Competence'];

    function CompetenceController ($scope, $state, Competence) {
        var vm = this;
        
        vm.competences = [];

        loadAll();

        function loadAll() {
            Competence.query(function(result) {
                vm.competences = result;
            });
        }
    }
})();
