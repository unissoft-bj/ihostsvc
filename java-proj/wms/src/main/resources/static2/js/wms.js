var myUIRoute = angular.module('MyWms', ['ui.router', 'ngAnimate']);
myUIRoute.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index");
    $stateProvider
        .state('index', {
            url: "/index",
            templateUrl: "tpls/main.htm",
            controller: function($scope,$http) {
                $http.get("rest/user1.json")
                .success(function(response) {$scope.user = response;})
                .error(function() {
                    //alert("--3--");
                     window.location.href="#/login"; 
                });
            }
        })
        
        .state('login', {
            url: "/login",
            templateUrl: "tpls/login.htm"
        })
        ;
});
