(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('donnees-collaborateur', {
            parent: 'entity',
            url: '/donnees-collaborateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DonneesCollaborateurs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donnees-collaborateur/donnees-collaborateurs.html',
                    controller: 'DonneesCollaborateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('donnees-collaborateur-detail', {
            parent: 'entity',
            url: '/donnees-collaborateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DonneesCollaborateur'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donnees-collaborateur/donnees-collaborateur-detail.html',
                    controller: 'DonneesCollaborateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DonneesCollaborateur', function($stateParams, DonneesCollaborateur) {
                    return DonneesCollaborateur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('donnees-collaborateur.new', {
            parent: 'donnees-collaborateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-collaborateur/donnees-collaborateur-dialog.html',
                    controller: 'DonneesCollaborateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descriptionRubriqueCollaborateur: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('donnees-collaborateur', null, { reload: true });
                }, function() {
                    $state.go('donnees-collaborateur');
                });
            }]
        })
        .state('donnees-collaborateur.edit', {
            parent: 'donnees-collaborateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-collaborateur/donnees-collaborateur-dialog.html',
                    controller: 'DonneesCollaborateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DonneesCollaborateur', function(DonneesCollaborateur) {
                            return DonneesCollaborateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('donnees-collaborateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('donnees-collaborateur.delete', {
            parent: 'donnees-collaborateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donnees-collaborateur/donnees-collaborateur-delete-dialog.html',
                    controller: 'DonneesCollaborateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DonneesCollaborateur', function(DonneesCollaborateur) {
                            return DonneesCollaborateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('donnees-collaborateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
