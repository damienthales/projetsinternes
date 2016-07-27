(function() {
    'use strict';

    angular
        .module('testApp')
        .factory('CollaborateurSearch', CollaborateurSearch);

    CollaborateurSearch.$inject = ['$resource'];

    function CollaborateurSearch($resource) {
        var resourceUrl =  'api/_search/collaborateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
