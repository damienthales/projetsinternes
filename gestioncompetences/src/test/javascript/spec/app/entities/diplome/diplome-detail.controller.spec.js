'use strict';

describe('Controller Tests', function() {

    describe('Diplome Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDiplome, MockCollaborateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDiplome = jasmine.createSpy('MockDiplome');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Diplome': MockDiplome,
                'Collaborateur': MockCollaborateur
            };
            createController = function() {
                $injector.get('$controller')("DiplomeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:diplomeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
