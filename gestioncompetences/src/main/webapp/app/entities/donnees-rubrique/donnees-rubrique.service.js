(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('DonneesRubrique', DonneesRubrique);

    DonneesRubrique.$inject = ['$resource', 'DateUtils'];

    function DonneesRubrique ($resource, DateUtils) {
        var resourceUrl =  'api/donnees-rubriques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.donneesRubriqueDateDebut = DateUtils.convertDateTimeFromServer(data.donneesRubriqueDateDebut);
                        data.donneesRubriqueDateFin = DateUtils.convertDateTimeFromServer(data.donneesRubriqueDateFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
