(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('classification', {
            parent: 'entity',
            url: '/classification',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Classifications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classification/classifications.html',
                    controller: 'ClassificationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('classification-detail', {
            parent: 'entity',
            url: '/classification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Classification'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classification/classification-detail.html',
                    controller: 'ClassificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Classification', function($stateParams, Classification) {
                    return Classification.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('classification.new', {
            parent: 'gestion-collaborateurs-detail',
            url: '/classification/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classification/classification-dialog.html',
                    controller: 'ClassificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                classificationNom: null,
                                classificationDateDebut: null,
                                classificationDateFin: null,
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
        .state('classification.edit', {
            parent: 'gestion-collaborateurs-detail',
            url: '/classification/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classification/classification-dialog.html',
                    controller: 'ClassificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classification', function(Classification) {
                            return Classification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gestion-collaborateurs-detail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classification.delete', {
            parent: 'gestion-collaborateurs-detail',
            url: '/classification/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classification/classification-delete-dialog.html',
                    controller: 'ClassificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Classification', function(Classification) {
                            return Classification.get({id : $stateParams.id}).$promise;
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
