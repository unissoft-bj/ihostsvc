angular.module('wms', [ 'ngRoute' ]).config(function($routeProvider) {

	//'localhost:8080'
		
	$routeProvider.when('/', {
		templateUrl : 'tpls/home.html',
		controller : 'home'
	}).when('/login', {
		templateUrl : 'tpls/login.html',
		controller : 'navigation'
	}).otherwise('/');

}).controller('navigation',

function($rootScope, $scope, $http, $location, $route) {

	$scope.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	var authenticate = function(callback) {

		$http.get('http://localhost:8080' + '/secure/user').success(function(data) {
			if (data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback();
		}).error(function() {
			$rootScope.authenticated = false;
			callback && callback();
		});

	}

	authenticate();

	$scope.credentials = {};
	$scope.login = function() {
		console.log($scope.credentials);
		$http.post('http://localhost:8080' + '/login', $.param($scope.credentials), {
			headers : {
				"content-type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data) {
			authenticate(function() {
				if ($rootScope.authenticated) {
					console.log("Login succeeded")
					$location.path("/");
					$scope.error = false;
					$rootScope.authenticated = true;
				} else {
					console.log("Login failed with redirect")
					$location.path("/login");
					$scope.error = true;
					$rootScope.authenticated = false;
				}
			});
		}).error(function(data) {
			console.log("Login failed")
			$location.path("/login");
			$scope.error = true;
			$rootScope.authenticated = false;
		})
	};

	$scope.logout = function() {
		$http.post('http://localhost:8080' + '/logout', {}).success(function() {
			$rootScope.authenticated = false;
			$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	}

}).controller('home', function($scope, $http) {
	$http.get('http://localhost:8080' + '/secure/resource').success(function(data) {
		$scope.greeting = data;
	})
});
