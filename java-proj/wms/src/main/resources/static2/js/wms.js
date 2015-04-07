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

                $scope.logout = function(){
                    $http.get("/logout")
                    .success(function(response){
                        alert("退出成功");
                        window.location.href="#/login1";
                    })
                    .error(function(){
                        alert("未知错误");
                    });

                };

            }
        })

        
        //手动登陆页面
        .state('login1', {
            url: "/login1",
            templateUrl: "tpls/login1.htm",
            controller:function($scope,$http){
                $scope.formData = {};

                $scope.login = function(){
                    // alert($scope.formData.phone);
                    // alert($scope.formData.acount_pwd);
                    $http.post("rest/login.php",$scope.formData)
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
            }
        })
        .state('login2', {
            url: "/login2",
            templateUrl: "tpls/login2.htm",
            controller:function($scope,$http){
                $scope.formData = {};
                $scope.msg="请输入登陆信息";
                $scope.register = function(){
                    // alert($scope.formData.shangwangma);
                    $http.post("rest/register.php",$scope.formData)
                    .success(function(response) {

                        window.location.href="#/main";
                   
                    })
                    .error(function() {
                        $scope.msg="登陆失败，请重新输入上网码";
                        window.location.href="#/login2";
                    });
                    
                }
            }
        })
        //清理磁盘。。。
        .state('disk', {
            url: "/disk",
            templateUrl: "tpls/disk.htm",
            controller:function($scope,$http){
                $scope.formData = {};
                $scope.msg="-";
                $http.get("rest/disk.json")
                .success(function(response){
                    $scope.msg=response;
                });

                $scope.cleandisk = function(){
                     //alert($scope.formData.days);
                    $http.post("rest/cleandisk.php",$scope.formData)
                    .success(function(response) {
                        alert("请求已提交。。。");
                        window.location.href="#/disk";
                   
                    })
                    .error(function() {
                        //$scope.msg="，请重新输入上网码";
                        window.location.href="#/disk";
                    });
                    
                }
            }
        })

        //发放上网码
        .state('shangwangma', {
            url: "/shangwangma",
            templateUrl: "tpls/shangwangma.htm",
            controller:function($scope,$http){
                $scope.formData = {"userrole":"1","type":"web"};
                $scope.msg="未生成";
                

                $scope.create_net_code = function(){                     
                    console.log($scope.formData.phone);
                    console.log($scope.formData.userrole);
                    console.log($scope.formData.type);
                    $http.post("rest/shangwangma.php",$scope.formData)
                    .success(function(response) {
                        alert("上网码生成成功");
                        $scope.msg=response;
                    })
                    .error(function() {
                        $scope.msg="未知错误，请重新生成";
                        //window.location.href="#/disk";
                    });
                    
                }
            }
        })
        ;
});
