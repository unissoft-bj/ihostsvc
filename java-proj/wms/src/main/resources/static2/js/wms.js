var myUIRoute = angular.module('MyWms', ['ui.router', 'ngAnimate']);
myUIRoute.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index");
    $stateProvider
        //测试页面
        .state('test', {
            url: "/test",
            templateUrl: "tpls/test.htm",
            controller: function($scope,$http) {

                $scope.userinfo = {"phone":"13333333333","account-pwd":"null","mac-pwd":"null"};
                $http.post("rest/login.php",$scope.userinfo)
                .success(function(response) {
                    //$scope.hello = response;
                    //todo将ihost信息写入全局变量 
                })
                .error(function() {
                    //alert("--3--");
                     window.location.href="#/login"; 
                });

                
                
            }

           
        })
        //首页判断自动登陆
        .state('index', {
            url: "/index",
            templateUrl: "tpls/index.htm",
            controller: function($scope,$http) {


                $http.get("rest/ihost.json")
                .success(function(response) {
                    $scope.ihost = response;
                    //todo将ihost信息写入全局变量 
                })
                .error(function() {
                    //alert("--3--");
                     window.location.href="#/login"; 
                });

                //todo，根据ihost id，将本地cookie中的 phone、account-pwd、mac-pwd post到/login
                $scope.userinfo = {"phone":"13333333333","account-pwd":"null","mac-pwd":"null"};
                $http.post("rest/login.php",$scope.userinfo)
                .success(function(response) {
                    //$scope.hello = response;
                    //stat1:1，成功，显示用户工作页面
                    if (response==1) {
                        window.location.href="#/main";
                    }else if (response==2) {
                        window.location.href="#/login1";
                    }else if (response==3) {
                        window.location.href="#/login2";
                    }else{
                        alert("非法操作");
                    } 
                    //stat1:2，跳转到手机号页面
                    //stat1:3，跳转到上网码页面
                })
                .error(function() {
                    //alert("--3--");
                    alert("非法操作");
                });
                
            }
        })

        //用户操作主页面
        .state('main', {
            url: "/main",
            templateUrl: "tpls/main.htm",
            controller: function($scope,$http) {
                $http.get("rest/user.json")
                .success(function(response) {$scope.user = response;})
                .error(function() {
                    //alert("--3--");
                     window.location.href="#/login"; 
                });
            }
        })

        
        //手动登陆页面
        .state('login1', {
            url: "/login1",
            templateUrl: "tpls/login1.htm"
        })
        .state('login2', {
            url: "/login2",
            templateUrl: "tpls/login2.htm"
        })
        ;
});
