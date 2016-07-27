(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rubrique', {
            parent: 'admin',
            url: '/rubrique',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rubriques'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rubrique/rubriques.html',
                    controller: 'RubriqueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rubrique-detail', {
            parent: 'admin',
            url: '/rubrique/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rubrique'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rubrique/rubrique-detail.html',
                    controller: 'RubriqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Rubrique', function($stateParams, Rubrique) {
                    return Rubrique.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('rubrique.new', {
            parent: 'admin',
            url: '/rubrique/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rubrique/rubrique-dialog.html',
                    controller: 'RubriqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rubriqueLibelle: null,
                                rubriqueTypeRubrique: null,
                                rubriqueObligatoire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rubrique', null, { reload: true });
                }, function() {
                    $state.go('rubrique');
                });
            }]
        })
        .state('rubrique.edit', {
            parent: 'rubrique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rubrique/rubrique-dialog.html',
                    controller: 'RubriqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rubrique', function(Rubrique) {
                            return Rubrique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rubrique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rubrique.delete', {
            parent: 'rubrique',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rubrique/rubrique-delete-dialog.html',
                    controller: 'RubriqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rubrique', function(Rubrique) {
                            return Rubrique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rubrique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
