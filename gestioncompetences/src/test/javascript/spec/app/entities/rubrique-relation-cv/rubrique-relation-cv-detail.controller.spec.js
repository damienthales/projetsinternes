'use strict';

describe('Controller Tests', function() {

    describe('RubriqueRelationCV Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRubriqueRelationCV, MockCv, MockRubrique;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRubriqueRelationCV = jasmine.createSpy('MockRubriqueRelationCV');
            MockCv = jasmine.createSpy('MockCv');
            MockRubrique = jasmine.createSpy('MockRubrique');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RubriqueRelationCV': MockRubriqueRelationCV,
                'Cv': MockCv,
                'Rubrique': MockRubrique
            };
            createController = function() {
                $injector.get('$controller')("RubriqueRelationCVDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:rubriqueRelationCVUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
