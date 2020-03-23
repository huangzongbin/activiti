<#assign basePath=request.contextPath />
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>xxxx系统</title>
    <link rel="icon" type="image/ico" href="${basePath}/plugin/res/images/favicon.ico" />
    <link rel="stylesheet" href="${basePath}/plugin/res/css/reset.css" />
    <link rel="stylesheet" href="${basePath}/plugin/res/css/common.css" />
    <link rel="stylesheet" href="${basePath}/plugin/res/css/login.css" />

    <!--[if lt IE 9]>
    <script src="${basePath}/plugin/res/lib/html5shiv.min.js"></script>
    <script src="${basePath}/plugin/res/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <div class="loginWrap clearfix">
        <i class="loginLCorner"></i>
        <div class="loginOutBox">
            <div class="loginBox">
                <h1 class="loginTit">xxxx系统</h1>
                <h2 class="loginSubTit">Jxxxx系统</h2>
                <div class="loginFormWrap">
                    <div class="loginFormRow">
                        <i class="loginUIcon"></i>
                        <input type="text" class="loginInput username" name="username" id="username" placeholder="请输入您的手机号码" maxlength="100" autocomplete="off" />
                        <i class="inputDelIcon" id="inputDel"></i>
                    </div>
                    <div class="loginFormRow">
                        <i class="loginPIcon"></i>
                        <input type="password" class="loginInput password" name="password" id="password" placeholder="请输入您的密码" maxlength="50" autocomplete="off" />
                        <i class="eyeIcon open" id="eyeControl"></i>
                    </div>
                    <div class="loginFormRow">
                        <input type="text" class="loginInput verifyCode" name="verifyCode" id="verifyCode" placeholder="图形验证码" maxlength="10" autocomplete="off" />
                        <span class="verifyCodeImg"><img id="verifyCodeImg" src="${basePath}/captcha.jpg" height="100%" width="117" onclick="changeImg();"/></span>
                    </div>
                    <div class="loginBtnWrap"><input type="button" name="loginBtn" id="loginBtn" class="loginBtn" value="登陆"/></div>
                    <p class="otherLink tr"><a href="#" class="link">忘记密码？</a></p>
                </div>
            </div>
            <i class="loginRCorner"></i>
            <div class="loginRPanel">
                <i class="folderImg"></i>
                <i class="boardImg"></i>
                <i class="phoneTableImg"></i>
                <i class="paperImg"></i>
            </div>
        </div>
    </div>
</div>
<script src="plugin/res/lib/jquery-1.12.4.min.js"></script>
<script src="${basePath}/plugin/jigsaw/jigsaw.js"></script>

<script src="${basePath}/plugin/layer/layer.js"></script>
<script src="${basePath}/dist/js/krt.js"></script>
<script type="text/javascript">
    $(function(){
        $('.loginFormRow').on('focus', '.loginInput', function(){
            $(this).parent().addClass('cur');
        });
        $('.loginFormRow').on('blur', '.loginInput', function(){
            $(this).parent().removeClass('cur');
        });
        $('#inputDel').on('click', function(e){
            $('#username').val('');
        });
        $('#eyeControl').on('click', function(e){
            if($(this).hasClass('open')) {
                $(this).removeClass('open').addClass('close');
                $('#password').attr('type', 'text');
            } else {
                $(this).removeClass('close').addClass('open');
                $('#password').attr('type', 'password');
            }
        });

        //登录按钮
        $('#loginBtn').on('click',function(){
            var verifyCode =  $("#verifyCode").val();
            var username = $("#username").val();
            var password = $("#password").val();
            if (username == null || username == '') {
                krt.layer.msg("手机号不可为空");
                return false;
            }
            if (password == null || password == '') {
                krt.layer.msg("密码不可为空");
                return false;
            }
            krt.ajax({
                url: "${basePath}/login",
                type: "POST",
                data: {
                    "username": username,
                    "password": password,
                    "ticket" : verifyCode
                },
                success: function (rb) {
                    if (rb.code == 200) {
                        location.href = "${basePath}/index";
                    } else {
                        krt.layer.msg(rb.msg);
                        //刷新图片
                        changeImg();
                    }
                }
            });
        })

    })

    //刷新验证码
    function changeImg() {
        var verifyCode=$("#verifyCode");
        verifyCode.val("");//清空验证码输入区
        verifyCode.focus();//验证码输入区获取焦点
        var imgSrc = $("#verifyCodeImg");
        var src = imgSrc.attr("src");
        console.log(src);
        imgSrc.attr("src", chgUrl(src));
    }

    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        console.log(timestamp);
        var urls = url.split("?");
        url = urls[0] + "?timestamp=" + timestamp;
        return url;
    }
</script>
</body>
</html>
