'use strict';

describe('Controller Tests', function() {

    describe('DonneesRubrique Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDonneesRubrique, MockCv, MockRubrique;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDonneesRubrique = jasmine.createSpy('MockDonneesRubrique');
            MockCv = jasmine.createSpy('MockCv');
            MockRubrique = jasmine.createSpy('MockRubrique');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DonneesRubrique': MockDonneesRubrique,
                'Cv': MockCv,
                'Rubrique': MockRubrique
            };
            createController = function() {
                $injector.get('$controller')("DonneesRubriqueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:donneesRubriqueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
