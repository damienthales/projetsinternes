'use strict';

describe('Controller Tests', function() {

    describe('CV Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCV, MockCollaborateur, MockRubrique;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCV = jasmine.createSpy('MockCV');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            MockRubrique = jasmine.createSpy('MockRubrique');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CV': MockCV,
                'Collaborateur': MockCollaborateur,
                'Rubrique': MockRubrique
            };
            createController = function() {
                $injector.get('$controller')("CVDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:cVUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
