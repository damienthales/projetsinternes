'use strict';

describe('Controller Tests', function() {

    describe('Collaborateur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCollaborateur, MockEmail, MockAdresse, MockFonction, MockClassification, MockFormation, MockPublication, MockDiplome, MockCv;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCollaborateur = jasmine.createSpy('MockCollaborateur');
            MockEmail = jasmine.createSpy('MockEmail');
            MockAdresse = jasmine.createSpy('MockAdresse');
            MockFonction = jasmine.createSpy('MockFonction');
            MockClassification = jasmine.createSpy('MockClassification');
            MockFormation = jasmine.createSpy('MockFormation');
            MockPublication = jasmine.createSpy('MockPublication');
            MockDiplome = jasmine.createSpy('MockDiplome');
            MockCv = jasmine.createSpy('MockCv');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Collaborateur': MockCollaborateur,
                'Email': MockEmail,
                'Adresse': MockAdresse,
                'Fonction': MockFonction,
                'Classification': MockClassification,
                'Formation': MockFormation,
                'Publication': MockPublication,
                'Diplome': MockDiplome,
                'Cv': MockCv
            };
            createController = function() {
                $injector.get('$controller')("CollaborateurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestioncompetencesApp:collaborateurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
