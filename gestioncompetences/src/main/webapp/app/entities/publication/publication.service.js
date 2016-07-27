(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Publication', Publication);

    Publication.$inject = ['$resource', 'DateUtils'];

    function Publication ($resource, DateUtils) {
        var resourceUrl =  'api/publications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.publicationDate = DateUtils.convertDateTimeFromServer(data.publicationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
