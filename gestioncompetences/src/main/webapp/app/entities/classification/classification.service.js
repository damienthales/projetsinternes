(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Classification', Classification);

    Classification.$inject = ['$resource', 'DateUtils'];

    function Classification ($resource, DateUtils) {
        var resourceUrl =  'api/classifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.classificationDateDebut = DateUtils.convertDateTimeFromServer(data.classificationDateDebut);
                        data.classificationDateFin = DateUtils.convertDateTimeFromServer(data.classificationDateFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
