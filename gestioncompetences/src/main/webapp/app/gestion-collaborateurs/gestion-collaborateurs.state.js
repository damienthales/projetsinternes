(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gestion-collaborateurs', {
            parent: 'app',
            url: '/gestion-collaborateurs',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Gestion des Collaborateurs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/gestion-collaborateurs/gestion-collaborateurs.html',
                    controller: 'GestionCollaborateursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('gestion-collaborateurs-detail', {
            parent: 'gestion-collaborateurs',
            url: '/collaborateur-detail/{idCollaborateur}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collaborateur'
            },
            views: {
                'content@': {
                    templateUrl: 'app/gestion-collaborateurs/gestion-collaborateurs-detail.html',
                    controller: 'CollaborateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Collaborateur', function($stateParams, Collaborateur) {
                    return Collaborateur.get({id : $stateParams.idCollaborateur}).$promise;
                }]
            }
        })
        .state('gestion-collaborateurs.new', {
            parent: 'gestion-collaborateurs',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/gestion-collaborateurs/gestion-collaborateurs-dialog.html',
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
                    $state.go('gestion-collaborateurs', null, { reload: true });
                }, function() {
                    $state.go('gestion-collaborateurs');
                });
            }]
        })
        .state('gestion-collaborateurs.edit', {
            parent: 'gestion-collaborateurs',
            url: '/{idCollaborateur}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/gestion-collaborateurs/gestion-collaborateurs-dialog.html',
                    controller: 'CollaborateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collaborateur', function(Collaborateur) {
                            return Collaborateur.get({id : $stateParams.idCollaborateur}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gestion-collaborateurs.delete', {
            parent: 'gestion-collaborateurs',
            url: '/{idCollaborateur}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/gestion-collaborateurs/gestion-collaborateurs-delete-dialog.html',
                    controller: 'CollaborateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Collaborateur', function(Collaborateur) {
                            return Collaborateur.get({id : $stateParams.idCollaborateur}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
