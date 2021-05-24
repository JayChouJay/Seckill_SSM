<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/login.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <title>登录|Login</title>
    <c:if test="${msg!=null}">
        <script>
            alert("${msg}");
        </script>
    </c:if>

</head>

<body>
<div class="login-box">
    <h2>Login</h2>
    <form id="regForm" action="/seckill/login" method="post">
        <div class="user-box">
            <input id="mobile" type="text" name="mobile" required>
            <label>用户名</label>
        </div>
        <div class="user-box">
            <input id="pass" type="password" name="pass" required>
            <label>密码</label>
        </div>
        <a class="btnLogin" onclick="subA()" >
            <span>周</span>
            <span>杰</span>
            <span>伦</span>
            <span>是</span>
            提交
        </a>
        <a class="btnLogin" href="/regist.jsp" style="float: right">
            <span>神</span>
            <span>精</span>
            <span>病</span>
            <span>！</span>
            注册
        </a>
    </form>
</div>
<script type="application/javascript">
    function subA() {
        if ($("#mobile").val().length != 11) {
            alert("电话号码不是11位");
            return false;
        }
        if (!$("#pass").val()) {
            alert("密码不能为空！！");
            return false;
        }
        $("#regForm").submit();
    }
</script>

<%--<div class="container">--%>
    <%--<form class="form-signin" action="/seckill/login" method="post">--%>
        <%--<h2 class="form-signin-heading">请登录</h2>--%>
        <%--<label for="phone">电话号码</label>--%>
        <%--<input type="text" id="phone" name="mobile" class="input-block-level" required autofocus placeholder="电话">--%>
        <%--<label for="pass">密码</label>--%>
        <%--<input type="password" id="pass" name="pass" class="input-block-level" placeholder="密码">--%>
        <%--<label class="checkbox">--%>
            <%--<input type="checkbox" value="remember-me"> Remember me--%>
        <%--</label>--%>
        <%--<button class="btn btn-large btn-primary" type="submit">登录</button>--%>
        <%--<button id="registButton" class="btn btn-large btn-primary">注册</button>--%>
    <%--</form>--%>

<%--</div> <!-- /container -->--%>

</body>
</html>
