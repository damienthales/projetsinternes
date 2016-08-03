(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('GestionCollaborateursController', GestionCollaborateursController);

    GestionCollaborateursController.$inject = ['$scope', '$state', 'DataUtils', 'Collaborateur', 'CollaborateurSearch', 'ParseLinks', 'AlertService'];

    function GestionCollaborateursController ($scope, $state, DataUtils, Collaborateur, CollaborateurSearch, ParseLinks, AlertService) {
        var vm = this;
        
        vm.collaborateurs = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'collaborateurNumeroTgi';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll () {
        	if (vm.currentSearch) {
        		CollaborateurSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Collaborateur.query({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            }
        	function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'collaborateurNumeroTgi') {
                    result.push('collaborateurNumeroTgi');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.collaborateurs.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.collaborateurs = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
        
        function clear () {
            vm.collaborateurs = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = 'collaborateurNumeroTgi';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.collaborateurs = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }
    }
    
    
    
    
})();
