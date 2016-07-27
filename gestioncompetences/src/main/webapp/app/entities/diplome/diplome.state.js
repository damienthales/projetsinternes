(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diplome', {
            parent: 'entity',
            url: '/diplome',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diplomes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diplome/diplomes.html',
                    controller: 'DiplomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('diplome-detail', {
            parent: 'entity',
            url: '/diplome/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diplome'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diplome/diplome-detail.html',
                    controller: 'DiplomeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Diplome', function($stateParams, Diplome) {
                    return Diplome.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('diplome.new', {
            parent: 'gestion-collaborateurs-detail',
            url: '/diplome/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diplome/diplome-dialog.html',
                    controller: 'DiplomeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                diplomeNom: null,
                                diplomeDate: null,
                                id: null,
                                idCollaborateur: $stateParams.idCollaborateur
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs-detail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diplome.edit', {
            parent: 'gestion-collaborateurs-detail',
            url: '/diplome/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diplome/diplome-dialog.html',
                    controller: 'DiplomeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diplome', function(Diplome) {
                            return Diplome.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs-detail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diplome.delete', {
            parent: 'gestion-collaborateurs-detail',
            url: '/diplome/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diplome/diplome-delete-dialog.html',
                    controller: 'DiplomeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Diplome', function(Diplome) {
                            return Diplome.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs-detail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
