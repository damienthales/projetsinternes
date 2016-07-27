(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('DonneesCollaborateur', DonneesCollaborateur);

    DonneesCollaborateur.$inject = ['$resource'];

    function DonneesCollaborateur ($resource) {
        var resourceUrl =  'api/donnees-collaborateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
