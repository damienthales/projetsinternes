'use strict';

describe('Controller Tests', function() {

    describe('Fonction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFonction, MockCollaborateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFonction = jasmine.createSpy('MockFonction');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fonction': MockFonction,
                'Collaborateur': MockCollaborateur
            };
            createController = function() {
                $injector.get('$controller')("FonctionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:fonctionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
