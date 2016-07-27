(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email', {
            parent: 'entity',
            url: '/email',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Emails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email/emails.html',
                    controller: 'EmailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('email-detail', {
            parent: 'entity',
            url: '/email/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Email'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email/email-detail.html',
                    controller: 'EmailDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Email', function($stateParams, Email) {
                    return Email.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('email.new', {
            parent: 'gestion-collaborateurs-detail',
            url: '/email/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-dialog.html',
                    controller: 'EmailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                emailLibelle: null,
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
        .state('email.edit', {
            parent: 'gestion-collaborateurs-detail',
            url: '/email/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-dialog.html',
                    controller: 'EmailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Email', function(Email) {
                            return Email.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email.delete', {
            parent: 'gestion-collaborateurs-detail',
            url: '/email/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-delete-dialog.html',
                    controller: 'EmailDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Email', function(Email) {
                            return Email.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
