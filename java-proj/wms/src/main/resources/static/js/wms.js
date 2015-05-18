var myUIRoute = angular.module('MyWms', ['ui.router', 'ngAnimate','ngCookies']);
myUIRoute.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index");
    $stateProvider
        //测试页面
        .state('test', {
            url: "/test",
            templateUrl: "tpls/test.htm",
            controller: function($cookieStore,$scope) {
               
                $cookieStore.put("persion", {
                        name: "my name",
                        age: 18
                });
                p = "persion";
                $scope.hello=$cookieStore.get(p);                
            }           
        })

        //首页判断自动登陆
        .state('index', {
            url: "/index",
            templateUrl: "tpls/index.htm",
            controller: function($scope,$http,$cookieStore) {
                alert($cookieStore.get("ihost"));  
                $http.get("ihost").success(function(data){
                    //alert(data.uuidHex);
                    $cookieStore.put("ihost",data); 

                });                       
                
                $http.get("mymac").success(function(data){
                    //alert(data);
                    //$cookieStore.put("mymac",data);
                    $cookieStore.put("mymac","60-36-DD-FF-FF-BB");
                }).error(function(){
                    //alert("得不到mac地址");
                    $cookieStore.put("mymac","60-36-DD-FF-FF-BB");
                }); 

                //自动登陆
                    alert("自动登陆开始");
                    
                    if($cookieStore.get("user")==null){
                        window.location.href="#/login1"; 
                    }
                    var autouser = $cookieStore.get("user");
                    //%7B%22name%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac_pwd%22%3A%222227b540-9079-4141-9f79-ea811bba85ab%22%2C%22role%22%3A%22UNKNOWN%22%2C%22phone%22%3A%222006548878%22%7D
                    var loginData = "username="+autouser.mac+"&password="+autouser.mac_pwd;
                    
                    $http.post("login",loginData,{
                        headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                        }
                    }).success(function(response) {                    
                        window.location.href="#/main";                    
                    }).error(function(response) {
                        alert("非法操作");
                        window.location.href="#/login1"; 
                        //alert(response);
                        if (response.code=="phone_login") {
                            alert("phone_login");
                        } else{
                            alert("非法操作");
                        };
                        
                    });


            }
        })

        //用户操作主页面
        .state('main', {
            url: "/main",
            templateUrl: "tpls/main.htm",
            controller: function($scope,$http,$cookieStore) {

                $http.get("secure/user")
                .success(function(response) {
                    //alert(response.name);
                    $scope.user = response;                    
                    //将用户信息放到cookie中
                    $cookieStore.put("user",response);                   
                    //alert($cookieStore.get("user").name);
                })
                .error(function() {
                     alert("非法登陆");
                     window.location.href="#/login1"; 
                });

                $scope.logout = function(){
                    $http.get("/logout")
                    .success(function(response){
                        alert("退出成功");
                        //...删除本地cookie
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
                //$scope.formData.username="60-36-DD-FF-FF-BB";
                
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    var loginData = "username=60-36-DD-FF-FF-BB&phone="+$scope.formData.phone+"&phone_pass="+$scope.formData.phone_pass;
                    $http.post("login",loginData,{
                        headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                        }
                    }).success(function(response) {                    
                        window.location.href="#/main";                    
                    }).error(function(response) {
                        alert("非法操作");
                        //alert(response);
                        if (response.code=="phone_login") {
                            alert("phone_login");
                        } else{
                            alert("非法操作");
                        };
                        
                    });
                }
            }
        })
        .state('login2', {
            url: "/login2",
            templateUrl: "tpls/login2.htm",
            controller:function($scope,$http){
                $scope.formData = {"mac":"C3-A1-C3-C4-C5-C6"};
                $scope.msg="请输入上网码";
                $scope.register = function(){
                    // alert($scope.formData.shangwangma);
                    $http.post("register",$scope.formData)
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
                $http.get("secure/disk")
                .success(function(response){
                    //$scope.msg=response.diskList[0].available;
                    $scope.msg=parseInt(response.diskList[0].available)*1024;
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
                $scope.formData = {"userRole":"ROLE_USER","type":"sms"};
                $scope.msg="未生成";
                

                $scope.create_net_code = function(){                     
                    console.log($scope.formData.phone);
                    console.log($scope.formData.userRole);
                    console.log($scope.formData.type);
                    $http.post("secure/shangwangma",$scope.formData)
                    .success(function(response) {
                        alert("上网码生成成功");
                        $scope.msg=response.token;
                    })
                    .error(function() {
                        $scope.msg="未知错误，请重新生成";
                        //window.location.href="#/disk";
                    });
                    
                }
            }
        })

        //答题页面
        .state('diaocha1', {
            url: "/diaocha1",
            templateUrl: "tpls/diaocha1.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha2";
                    
                }
            }
        })

        .state('diaocha2', {
            url: "/diaocha2",
            templateUrl: "tpls/diaocha2.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha3";
                    
                }
            }
        })

        .state('diaocha3', {
            url: "/diaocha3",
            templateUrl: "tpls/diaocha3.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha4";
                    
                }
            }
        })

        .state('diaocha4', {
            url: "/diaocha4",
            templateUrl: "tpls/diaocha4.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha5";
                    
                }
            }
        })

        .state('diaocha5', {
            url: "/diaocha5",
            templateUrl: "tpls/diaocha5.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha6";
                    
                }
            }
        })

        .state('diaocha6', {
            url: "/diaocha6",
            templateUrl: "tpls/diaocha6.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    window.location.href="#/diaocha6";
                    
                }
            }
        })
        
        ;
});
