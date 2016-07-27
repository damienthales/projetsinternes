(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('EmailController', EmailController);

    EmailController.$inject = ['$scope', '$state', 'Email'];

    function EmailController ($scope, $state, Email) {
        var vm = this;
        
        vm.emails = [];

        loadAll();

        function loadAll() {
            Email.query(function(result) {
                vm.emails = result;
            });
        }
    }
})();
