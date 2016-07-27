(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('telephone', {
            parent: 'entity',
            url: '/telephone',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Telephones'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/telephone/telephones.html',
                    controller: 'TelephoneController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('telephone-detail', {
            parent: 'entity',
            url: '/telephone/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Telephone'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/telephone/telephone-detail.html',
                    controller: 'TelephoneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Telephone', function($stateParams, Telephone) {
                    return Telephone.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('telephone.new', {
            parent: 'telephone',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/telephone/telephone-dialog.html',
                    controller: 'TelephoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                telephoneNumero: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('telephone', null, { reload: true });
                }, function() {
                    $state.go('telephone');
                });
            }]
        })
        .state('telephone.edit', {
            parent: 'telephone',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/telephone/telephone-dialog.html',
                    controller: 'TelephoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Telephone', function(Telephone) {
                            return Telephone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('telephone', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('telephone.delete', {
            parent: 'telephone',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/telephone/telephone-delete-dialog.html',
                    controller: 'TelephoneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Telephone', function(Telephone) {
                            return Telephone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('telephone', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
