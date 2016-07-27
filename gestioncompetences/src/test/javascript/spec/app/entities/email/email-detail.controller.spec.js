'use strict';

describe('Controller Tests', function() {

    describe('Email Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmail, MockCollaborateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmail = jasmine.createSpy('MockEmail');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Email': MockEmail,
                'Collaborateur': MockCollaborateur
            };
            createController = function() {
                $injector.get('$controller')("EmailDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:emailUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
