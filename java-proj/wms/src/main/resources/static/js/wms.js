function CurentTime()
    { 
        var now = new Date();
       
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
       
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
       
        var clock = year + "-";
       
        if(month < 10)
            clock += "0";
       
        clock += month + "-";
       
        if(day < 10)
            clock += "0";
           
        clock += day + " ";
       
        if(hh < 10)
            clock += "0";
           
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm; 
        return(clock+":00"); 
    } 

function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    } 
  }
return ""
}

function setCookie(c_name,value,expiredays)
{
var exdate=new Date()
exdate.setDate(exdate.getDate()+expiredays)
document.cookie=c_name+ "=" +escape(value)+
((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
}    

var answers = new Array();
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
                //alert($cookieStore.get("ihost"));  
                $http.get("ihost").success(function(data){
                    //alert(data.uuidHex);
                    
                    $cookieStore.put("ihost",data); 
                    //console.log(getCookie("ihost"));
                    setCookie("ihost",getCookie("ihost"),365);
                });                       
                
                $http.get("mymac").success(function(data){
                    //alert(data);
                    $cookieStore.put("mymac",data);
                    setCookie("mymac",getCookie("mymac"),365);
                    //$cookieStore.put("mymac","AA-36-DD-FF-FF-BB");
                }).error(function(){
                    alert("得不到mac地址");
                    
                    $cookieStore.put("mymac","AA-36-DD-FF-FF-BB");
                    setCookie("mymac",getCookie("mymac"),365);
                }); 

                //自动登陆
                    //alert("自动登陆开始");
                    
                    if($cookieStore.get("user")==null){
                        window.location.href="#/diaocha1"; 
                    }
                    var autouser = $cookieStore.get("user");
                    //%7B%22name%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac_pwd%22%3A%222227b540-9079-4141-9f79-ea811bba85ab%22%2C%22role%22%3A%22UNKNOWN%22%2C%22phone%22%3A%222006548878%22%7D
                    var loginData = "username="+autouser.mac+"&password="+autouser.mac_pwd;
                    
                    $http.post("login",loginData,{
                        headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                        }
                    }).success(function(response) {                    
                        window.location.href="#/diaocha_over";                    
                    }).error(function(response) {
                        alert("非法操作");
                        window.location.href="#/diaocha1"; 
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
                $scope.isshow=false;
                $http.get("secure/user")
                .success(function(response) {
                    //alert(response.name);
                    $scope.user = response; 
                    
                    if($scope.user.role=="ROLE_MANAGER"){
                        $scope.isshow=true;
                    }                  
                    //将用户信息放到cookie中
                    $cookieStore.put("user",response);
                    setCookie("user",getCookie("user"),365);                   
                    //alert($cookieStore.get("user").name);
                })
                .error(function() {
                     alert("非法登录");
                     window.location.href="#/login2"; 
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

                $scope.downFile = function(){

                    $http.get("/secure/survey/file")
                    .success(function(response){
                        //alert("退出成功");
                        //...删除本地cookie
                        //window.location.href="#/login1";
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
            controller:function($scope,$http,$cookieStore){
                    
                function autoLogin(){
                    var autouser = $cookieStore.get("user");
                    //%7B%22name%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac%22%3A%2260-36-DD-FF-FF-BB%22%2C%22mac_pwd%22%3A%222227b540-9079-4141-9f79-ea811bba85ab%22%2C%22role%22%3A%22UNKNOWN%22%2C%22phone%22%3A%222006548878%22%7D
                    if(autouser!=null){
                        var loginData = "username="+autouser.mac+"&password="+autouser.mac_pwd;
                        
                        $http.post("login",loginData,{
                            headers : {
                            "content-type" : "application/x-www-form-urlencoded"
                            }
                        }).success(function(response) {
                            if(autouser.role=="ROLE_MANAGER"){
                                window.location.href="#/main"; 
                            }                    
                                               
                        }).error(function(response) {
                            alert("请输入上网码");
                            
                            
                        });


                    }
                }    
                autoLogin();
                $http.get("mymac").success(function(data){
                    
                    $cookieStore.put("mymac",data);
                    setCookie("mymac",getCookie("mymac"),365);
                    //$cookieStore.put("mymac","AA-BB-DD-FF-FF-BB");
                }).error(function(){
                    alert("得不到mac地址");
                    $cookieStore.put("mymac","AA-BB-DD-FF-FF-BB");
                    setCookie("mymac",getCookie("mymac"),365);
                });
                $scope.formData = {};
                $scope.formData.mac = $cookieStore.get("mymac");
                $scope.msg="请输入上网码";
                $scope.register = function(){
                    // alert($scope.formData.shangwangma);
                    $http.post("register",$scope.formData)
                    .success(function(response) {
                        console.log(response);
                        $cookieStore.put("user",response);
                        setCookie("user",getCookie("user"),365);
                        autoLogin();
                        //window.location.href="#/login2";
                   
                    })
                    .error(function() {
                        $scope.msg="登录失败，请重新输入上网码";
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
                $scope.formData = {"q_id":1};            
                $scope.login = function(){
                    if($scope.formData.available_option==null){
                        alert("请至少选择一项");
                        return false;
                    }
                    $scope.formData.available_option = JSON.stringify($scope.formData.available_option);
                    answers.push($scope.formData);
                    console.log(answers);
                    window.location.href="#/diaocha2";
                    
                }
            }
        })

        .state('diaocha2', {
            url: "/diaocha2",
            templateUrl: "tpls/diaocha2.htm",
            controller:function($scope,$http){

                $scope.formData = {"q_id":2};
                console.log($scope.formData.available_option)            
                $scope.login = function(){
                    if($scope.formData.available_option==null){
                        alert("请至少选择一项");
                        return false;
                    }
                    console.log($scope.formData.available_option)
                    $scope.formData.available_option = JSON.stringify($scope.formData.available_option);
                    answers.push($scope.formData);
                    console.log(answers);
                    window.location.href="#/diaocha3";
                    
                }
            }
        })

        .state('diaocha3', {
            url: "/diaocha3",
            templateUrl: "tpls/diaocha3.htm",
            controller:function($scope,$http){
                $scope.formData = {"q_id":3};
                console.log($scope.formData.available_option)            
                $scope.login = function(){
                    if($scope.formData.available_option==null){
                        alert("请至少选择一项");
                        return false;
                    }
                    console.log($scope.formData.available_option)
                    $scope.formData.available_option = JSON.stringify($scope.formData.available_option);
                    answers.push($scope.formData);
                    console.log(answers);
                    window.location.href="#/diaocha4";
                    
                }
            }
        })

        .state('diaocha4', {
            url: "/diaocha4",
            templateUrl: "tpls/diaocha4.htm",
            controller:function($scope,$http){
                $scope.formData = {"q_id":4};
                $scope.formData.available_option=null;
                console.log($scope.formData.available_option)            
                $scope.login = function(){
                    if($scope.formData.available_option==null){
                        alert("请答题");
                        return false;
                    }
                    console.log($scope.formData.available_option)
                    $scope.formData.available_option = JSON.stringify($scope.formData.available_option);
                    answers.push($scope.formData);
                    console.log(answers);
                    window.location.href="#/diaocha5";
                    
                }
            }
        })

        .state('diaocha5', {
            url: "/diaocha5",
            templateUrl: "tpls/diaocha5.htm",
            controller:function($scope,$http){
                $scope.formData = {"q_id":5};
                console.log($scope.formData.available_option)            
                $scope.login = function(){
                    if($scope.formData.available_option==null){
                        alert("请至少选择一项");
                        return false;
                    }
                    console.log($scope.formData.available_option);
                    $scope.formData.available_option = JSON.stringify($scope.formData.available_option);
                    answers.push($scope.formData);
                    console.log(answers);
                    window.location.href="#/diaocha6";
                    
                }
            }
        })

        .state('diaocha6', {
            url: "/diaocha6",
            templateUrl: "tpls/diaocha6.htm",
            controller:function($scope,$http,$cookieStore){
                $scope.formData = {};
                $scope.formData.show_location= "Boston";
                console.log($scope.formData.available_option)
                $scope.login = function(){
                    if($scope.formData.name==null){
                        alert("请输入姓名");
                        return false;
                    }
                    if($scope.formData.age==null){
                        alert("请输入年龄");
                        return false;
                    }
                    if($scope.formData.gender==null){
                        alert("请选择性别");
                        return false;
                    }
                    if($scope.formData.city==null){
                        alert("请输入所在城市");
                        return false;
                    }
                    if($scope.formData.phone==null){
                        alert("请输入手机号");
                        return false;
                    }
                    if($scope.formData.has_car==null){
                        alert("请选择是否有私家车");
                        return false;
                    }
                    console.log($scope.formData)
                    //answers.push($scope.formData);
                    console.log(answers);

                    var postData = {};
                    postData.answers = answers;
                    
                    postData.surveyee = $scope.formData;
                    postData.mac = $cookieStore.get("mymac");

                    console.log(postData);

                    $http.post("survey",postData)
                    .success(function(response) {

                        console.log(response);
                        $cookieStore.put("user",response);
                        setCookie("user",getCookie("user"),365);
                        window.location.href="#/index";
                   
                    })
                    .error(function() {
                        alert("未知异常");
                        //$scope.msg="登陆失败，请重新输入上网码";
                        //window.location.href="#/login2";
                    });
                    //window.location.href="#/diaocha_over";
                    
                }
            }
        })

        .state('diaocha_over', {
            url: "/diaocha_over",
            templateUrl: "tpls/diaocha_over.htm",
            
        })

        //设置带宽
        .state('daikuan', {
            url: "/daikuan",
            templateUrl: "tpls/daikuan.htm",
            controller:function($scope,$http){
                $scope.formData = {};            
                $scope.setDaikuan = function(){
                     alert("设置带宽");
                    
                    
                }
            }
        })
        //设置奖池时间
        .state('lotteryTime', {
            url: "/lotteryTime",
            templateUrl: "tpls/lotteryTime.htm",
            controller:function($scope,$http,$cookieStore){
                $scope.formData = {};
                if($cookieStore.get("start_t")==null){
                    $scope.formData.start_t = CurentTime();
                }else{
                    $scope.formData.start_t = $cookieStore.get("start_t");
                }
                
                if($cookieStore.get("end_t")==null){
                    $scope.formData.end_t = CurentTime();
                }else{
                    $scope.formData.end_t = $cookieStore.get("end_t");
                }

                
                $scope.msg="请按照指定格式输入起止时间";
                $scope.setTime = function(){
                    $cookieStore.put("start_t",$scope.formData.start_t);
                    setCookie("start_t",getCookie("start_t"),365);
                    $cookieStore.put("end_t",$scope.formData.end_t);
                    setCookie("end_t",getCookie("end_t"),365);
                    
                    $http.post("secure/lottery/time",$scope.formData)
                    .success(function(response) {
                        console.log(response); 
                        $scope.msg="设置成功";                 
                    })
                    .error(function() {
                        alert("未知异常");
                        $scope.msg="设置失败，请重新登录";
                        //$scope.msg="登陆失败，请重新输入上网码";
                        //window.location.href="#/login2";
                    });
                    
                    
                }
            }
        })

        //奖池列表
        .state('lotteryList', {
            url: "/lotteryList",
            templateUrl: "tpls/lotteryList.htm",
            controller:function($scope,$http,$cookieStore){
                
                $http.get("secure/lottery")
                .success(function(response){
                    $scope.lottery = response;
                    console.log($scope.lottery);
                }).error(function(){
                    alert("获取奖池失败");
                });
                    
                    
            }
            
        })

        //奖池添加号码
        .state('lotteryAdd', {
            url: "/lotteryAdd",
            templateUrl: "tpls/lotteryAdd.htm",
            controller:function($scope,$http,$cookieStore){
                $scope.msg="多个手机号请用|分隔";
                $scope.formData={};
                $scope.addPhone = function(){
                    console.log($scope.formData.phone);
                    var phones = $scope.formData.phone.split("|");
                    console.log(phones);

                    $http.post("secure/lottery",phones)
                    .success(function(response){
                        alert("添加成功");
                        window.location.href="#/lotteryList";
                    }).error(function(){
                        alert("添加失败");
                    });
                    }    
                    
            }
            
        })


        //管理员界面
        .state('manage', {
            url: "/manage",
            templateUrl: "tpls/manage.htm",
            
            controller:function($scope,$http,$cookieStore){
                
                    
            }
        })

        //ihost登陆iserver
        .state('manage_login', {
            url: "/manage/login",
            templateUrl: "tpls/manage/login.htm",
            controller: function($scope,$http) {
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    var loginData = "username="+$scope.formData.username+"&iserver="+$scope.formData.iserver+"&pwd="+$scope.formData.pwd;
                    $http.post("/manage/login",loginData,{
                        headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                        }
                    }).success(function(response) {                    
                        window.location.href="#/manage/register";                    
                    }).error(function(response) {
                        window.location.href="#/manage/register";
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
        
        //ihost注册活动
        .state('manage_regiser', {
            url: "/manage/register",
            templateUrl: "tpls/manage/register.htm",
            controller: function($scope,$http) {
                $scope.people = [
                    {
                        id: 0,
                        Organization: '组织1',
                        project: [
                            {
                                id:0,
                                projectName:"项目1",
                                Activity:["活动11","活动12","活动13"]
                            },
                            {
                                id:1,
                                projectName:"项目2",
                                Activity:["活动21","活动22","活动23"]
                            },
                            {
                                id:2,
                                projectName:"项目3",
                                Activity:["活动31","活动32","活动33"]
                            },
                            {
                                id:3,
                                projectName:"项目4",
                                Activity:["活动41","活动42","活动43"]
                            }
                        ]
                    },
                    {
                        id: 1,
                        Organization: '组织2',
                        project: [
                            {
                                id:0,
                                projectName:"项目1",
                                Activity:["活动11","活动12","活动13"]
                            },
                            {
                                id:1,
                                projectName:"项目2",
                                Activity:["活动21","活动22","活动23"]
                            },
                            {
                                id:2,
                                projectName:"项目3",
                                Activity:["活动31","活动32","活动33"]
                            },
                            {
                                id:3,
                                projectName:"项目4",
                                Activity:["活动41","活动42","活动43"]
                            }
                        ]
                    },
                    {
                        id: 2,
                        Organization: '组织3',
                        project: [
                            {
                                id:0,
                                projectName:"项目1",
                                Activity:["活动11","活动12","活动13"]
                            },
                            {
                                id:1,
                                projectName:"项目2",
                                Activity:["活动21","活动22","活动23"]
                            },
                            {
                                id:2,
                                projectName:"项目3",
                                Activity:["活动31","活动32","活动33"]
                            },
                            {
                                id:3,
                                projectName:"项目4",
                                Activity:["活动41","活动42","活动43"]
                            }
                        ]
                    }
                ];
                $scope.login = function(){
                     //alert($scope.formData.phone_pass);
                    var loginData = "username="+$scope.formData.username+"&iserver="+$scope.formData.iserver+"&pwd="+$scope.formData.pwd;
                    $http.post("/manage/regiser",loginData,{
                        headers : {
                        "content-type" : "application/x-www-form-urlencoded"
                        }
                    }).success(function(response) {                    
                        window.location.href="#/manage/register";                    
                    }).error(function(response) {
                        window.location.href="#/manage/register";
                        alert("注册成功");
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
        
        //提交位置信息
        .state('manage_location', {
            url: "/manage/location",
            templateUrl: "tpls/manage/location.htm",

            controller: function($scope,$http) {
                $scope.formdata={};
                function getLocation()
                  {
                  if (navigator.geolocation)
                    {
                    navigator.geolocation.getCurrentPosition(showPosition);
                    }
                  else{x.innerHTML="Geolocation is not supported by this browser.";}
                  };
                function showPosition(position)
                  {
                    latitude = position.coords.latitude;
                    longitude = position.coords.longitude;
                    alert(latitude); 
                    alert(longitude);
                    $scope.formdata.location0=latitude+"|"+longitude;
                    
                  };
                
                $scope.setLocation = function(){                 
                    if (navigator.geolocation) {
                        console.log("浏览器支持!");
                        getLocation();                        
                     }
                    else {
                         console.log("浏览器不支持!");
                         alert("您的浏览器不支持位置信息");
                     }
                   
                }

                $scope.sendLocation = function(){
                     //alert($scope.formData.phone_pass);
                    
                    $http.post("/manage/location",$scope.formdata)
                    .success(function(response) {                    
                        alert($scope.formdata.location0);           
                    }).error(function(response) {
                        
                        alert($scope.formdata.location0);
                        
                        
                    });
                }
            }
        })
        
        //无线接入参数
        .state('manage_wirelessCfg', {
            url: "/manage/wirelessCfg",
            templateUrl: "tpls/manage/wirelessCfg.htm",
            controller: function($scope,$http) {
            
                $scope.people = [
                    {
                        id: 0,
                        Organization: '带宽',
                        values:'bandwidth',
                        
                    },
                    {
                        id: 1,
                        Organization: '时长',
                        values:'session',
                        
                    },
                    {
                        id: 2,
                        Organization: '宽限期',
                        values:'idle',
                        
                    },
                    {
                        id: 3,
                        Organization: '下发DNS',
                        values:'clientdns',
                        
                    },
                    {
                        id: 4,
                        Organization: '默许网站',
                        values:'garden',
                        
                    }
                ];

                $scope.login = function(){
                    postData = "ihostset.sh ";
                    postData+=$scope.selectedOrg.values+" ";
                    postData+=$scope.formData.para1+" ";
                    postData+=$scope.formData.para2+" ";
                    postData+=$scope.formData.para3+" ";
                    postData+=$scope.formData.para4+" ";
                    alert(postData);
                }
                
            }
        })
        
        //网络环境数据
        .state('manage_networkEnv', {
            url: "/manage/networkEnv",
            templateUrl: "tpls/manage/networkEnv.htm",
            controller: function($scope,$http) {
            
                $scope.project = [
                    {
                        id: 0,
                        Organization: '终端发现',
                        values:'wlansniff',
                        
                    },
                    {
                        id: 1,
                        Organization: '网址记录',
                        values:'lansniff',
                        
                    },
                    
                ];

                

                $scope.state = [
                    {
                        id: 0,
                        Organization: 'on',
                        values:'on',
                        
                    },
                    {
                        id: 1,
                        Organization: 'off',
                        values:'off',
                        
                    },
                    
                ];

                

                $scope.login = function(){
                    postData = "ihostset.sh ";
                    postData+=$scope.selectedOrg.values+" ";
                    postData+=$scope.selectedState.values+" ";
                    
                    alert(postData);
                }
                
            }
        })

        //DNS参数
        .state('manage_DNSCfg', {
            url: "/manage/DNSCfg",
            templateUrl: "tpls/manage/DNSCfg.htm",
            controller: function($scope,$http) {
            
                $scope.project = [
                    {
                        id: 0,
                        Organization: 'DNS强制',
                        values:'dnsspoof',
                        
                    },
                    {
                        id: 1,
                        Organization: '上位DNS',
                        values:'upstreamdns',
                        
                    },
                    
                ];

                $scope.login = function(){
                    postData = "ihostset.sh ";
                    postData+=$scope.selectedOrg.values+" ";
                    postData+=$scope.formData.para1+" ";
                    postData+=$scope.formData.para2+" ";
                    
                    alert(postData);
                }
                
            }
        })

        //组网模式
        .state('manage_netMode', {
            url: "/manage/netMode",
            templateUrl: "tpls/manage/netMode.htm",
            controller: function($scope,$http) {
                $scope.login = function(){
                    postData = "ihostmod.sh ";
                    
                    postData+=$scope.formData.para1+" ";
                    postData+=$scope.formData.para2+" ";
                    postData+=$scope.formData.para3+" ";
                    postData+=$scope.formData.para4+" ";
                    
                    alert(postData);
                }
                
            }
        })
        ;
});
