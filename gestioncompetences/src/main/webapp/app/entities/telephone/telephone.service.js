(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Telephone', Telephone);

    Telephone.$inject = ['$resource'];

    function Telephone ($resource) {
        var resourceUrl =  'api/telephones/:id';

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
