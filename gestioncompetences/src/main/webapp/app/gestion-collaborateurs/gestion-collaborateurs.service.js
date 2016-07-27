(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Collaborateur', Collaborateur);

    Collaborateur.$inject = ['$resource', 'DateUtils'];

    function Collaborateur ($resource, DateUtils) {
        var resourceUrl =  'api/gestion-collaborateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.collaborateurDateNaissance = DateUtils.convertDateTimeFromServer(data.collaborateurDateNaissance);
                        data.collaborateurDateArrivee = DateUtils.convertDateTimeFromServer(data.collaborateurDateArrivee);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
