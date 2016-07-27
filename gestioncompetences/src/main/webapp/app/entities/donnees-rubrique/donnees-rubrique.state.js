(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('donnees-rubrique', {
            parent: 'gestion-collaborateurs',
            url: '/donnees-rubrique',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DonneesRubriques'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donnees-rubrique/donnees-rubriques.html',
                    controller: 'DonneesRubriqueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('donnees-rubrique-detail', {
            parent: 'gestion-collaborateurs',
            url: '/donnees-rubrique/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DonneesRubrique'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donnees-rubrique/donnees-rubrique-detail.html',
                    controller: 'DonneesRubriqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DonneesRubrique', function($stateParams, DonneesRubrique) {
                    return DonneesRubrique.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('donnees-rubrique.new', {
            parent: 'cv-edit',
            url: '/donnees-rubrique/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-rubrique/donnees-rubrique-dialog.html',
                    controller: 'DonneesRubriqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                donneesRubriqueTitre: null,
                                donneesRubriqueOrdre: 1,
                                donneesRubriqueDateDebut: null,
                                donneesRubriqueDateFin: null,
                                donneesRubriquePoste: null,
                                donneesRubriqueClient: null,
                                donneesRubriqueNiveaucompetence: null,
                                donneesRubriqueDescription: null,
                                id: null,
                                idCv: $stateParams.id
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cv-edit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('donnees-rubrique.edit', {
            parent: 'cv-edit',
            url: '/donnees-rubrique/{idDonneeRubrique}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-rubrique/donnees-rubrique-dialog.html',
                    controller: 'DonneesRubriqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DonneesRubrique', function(DonneesRubrique) {
                            return DonneesRubrique.get({id : $stateParams.idDonneeRubrique}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cv-edit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('donnees-rubrique.delete', {
            parent: 'cv-edit',
            url: '/donnees-rubrique/{idDonneeRubrique}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-rubrique/donnees-rubrique-delete-dialog.html',
                    controller: 'DonneesRubriqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DonneesRubrique', function(DonneesRubrique) {
                            return DonneesRubrique.get({id : $stateParams.idDonneeRubrique}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cv-edit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
