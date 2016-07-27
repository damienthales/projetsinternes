(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Fonction', Fonction);

    Fonction.$inject = ['$resource', 'DateUtils'];

    function Fonction ($resource, DateUtils) {
        var resourceUrl =  'api/fonctions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fonctionDateDebut = DateUtils.convertDateTimeFromServer(data.fonctionDateDebut);
                        data.fonctionDateFin = DateUtils.convertDateTimeFromServer(data.fonctionDateFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
