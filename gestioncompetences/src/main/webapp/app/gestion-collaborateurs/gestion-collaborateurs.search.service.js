(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .factory('CollaborateurSearch', CollaborateurSearch);

    CollaborateurSearch.$inject = ['$resource'];

    function CollaborateurSearch($resource) {
        var resourceUrl =  'api/_search/gestion-collaborateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
