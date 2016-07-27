'use strict';

describe('Controller Tests', function() {

    describe('DonneesCollaborateur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDonneesCollaborateur, MockRubrique;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDonneesCollaborateur = jasmine.createSpy('MockDonneesCollaborateur');
            MockRubrique = jasmine.createSpy('MockRubrique');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DonneesCollaborateur': MockDonneesCollaborateur,
                'Rubrique': MockRubrique
            };
            createController = function() {
                $injector.get('$controller')("DonneesCollaborateurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:donneesCollaborateurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
