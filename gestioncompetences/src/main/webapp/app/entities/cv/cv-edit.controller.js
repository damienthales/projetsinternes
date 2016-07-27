(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('CvEditController', CvEditController);

    CvEditController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'entity', 'Cv', 'Collaborateur', 'Rubrique'];

    function CvEditController ($timeout, $scope, $stateParams, $state, entity, Cv, Collaborateur, Rubrique) {
        var vm = this;

        vm.cv = entity;
        vm.clear = clear;
        
        vm.predicate = 'donneesRubriqueTitre';
        vm.reset = reset;
        vm.reverse = true;
        
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cv.id !== null) {
                Cv.update(vm.cv, onSaveSuccess, onSaveError);
            } else {
                Cv.save(vm.cv, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestioncompetencesApp:cvUpdate', result);
            vm.isSaving = false;
            $state.transitionTo('gestion-collaborateurs-detail', $stateParams, {'reload':true});
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCv = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'donneesRubriqueTitre') {
                result.push('donneesRubriqueTitre');
            }
            return result;
        }
        
        function reset () {
            vm.page = 0;
            vm.collaborateurs = [];
            loadAll();
        }
    }
})();
