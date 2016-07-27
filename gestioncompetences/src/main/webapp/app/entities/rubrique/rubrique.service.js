(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Rubrique', Rubrique);

    Rubrique.$inject = ['$resource'];

    function Rubrique ($resource) {
        var resourceUrl =  'api/rubriques/:id';

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
