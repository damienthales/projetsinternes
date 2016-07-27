'use strict';

describe('Controller Tests', function() {

    describe('Cv Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCv, MockDonneesRubrique, MockCollaborateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCv = jasmine.createSpy('MockCv');
            MockDonneesRubrique = jasmine.createSpy('MockDonneesRubrique');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Cv': MockCv,
                'DonneesRubrique': MockDonneesRubrique,
                'Collaborateur': MockCollaborateur
            };
            createController = function() {
                $injector.get('$controller')("CvDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:cvUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
