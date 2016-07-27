(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collaborateur', {
            parent: 'entity',
            url: '/collaborateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collaborateurs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collaborateur/collaborateurs.html',
                    controller: 'CollaborateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('collaborateur-detail', {
            parent: 'entity',
            url: '/collaborateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collaborateur'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collaborateur/collaborateur-detail.html',
                    controller: 'CollaborateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Collaborateur', function($stateParams, Collaborateur) {
                    return Collaborateur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('collaborateur.new', {
            parent: 'collaborateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborateur/collaborateur-dialog.html',
                    controller: 'CollaborateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                collaborateurNom: null,
                                collaborateurPrenom: null,
                                collaborateurDateNaissance: null,
                                collaborateurSexe: null,
                                collaborateurEtatMarital: null,
                                collaborateurNombreEnfant: null,
                                collaborateurDateArrivee: null,
                                collaborateurPhotos: null,
                                collaborateurPhotosContentType: null,
                                collaborateurLangue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collaborateur', null, { reload: true });
                }, function() {
                    $state.go('collaborateur');
                });
            }]
        })
        .state('collaborateur.edit', {
            parent: 'collaborateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborateur/collaborateur-dialog.html',
                    controller: 'CollaborateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collaborateur', function(Collaborateur) {
                            return Collaborateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collaborateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collaborateur.delete', {
            parent: 'collaborateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collaborateur/collaborateur-delete-dialog.html',
                    controller: 'CollaborateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Collaborateur', function(Collaborateur) {
                            return Collaborateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collaborateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
