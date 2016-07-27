(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Formation', Formation);

    Formation.$inject = ['$resource', 'DateUtils'];

    function Formation ($resource, DateUtils) {
        var resourceUrl =  'api/formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.formationDate = DateUtils.convertDateTimeFromServer(data.formationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
