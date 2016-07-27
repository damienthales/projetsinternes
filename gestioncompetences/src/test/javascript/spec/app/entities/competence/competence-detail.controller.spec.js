'use strict';

describe('Controller Tests', function() {

    describe('Competence Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompetence, MockCollaborateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompetence = jasmine.createSpy('MockCompetence');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Competence': MockCompetence,
                'Collaborateur': MockCollaborateur
            };
            createController = function() {
                $injector.get('$controller')("CompetenceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:competenceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
