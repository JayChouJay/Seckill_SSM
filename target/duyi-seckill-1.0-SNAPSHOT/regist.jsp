<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/login.css">
    <title>注册|Register</title>
    <c:if test="${msg!=null}">
        <script>
            alert("${msg}");
        </script>
    </c:if>
</head>

<body>
<div class="login-box">
    <h2>Register</h2>
    <form id="regForm" action="/seckill/regist" method="post">
        <div class="user-box">
            <input id="mobile" type="text" name="mobile" required>
            <label>手机号码</label>
        </div>
        <div class="user-box">
            <input id="pass1" type="password" name="pass" required>
            <label>密码</label>
        </div>
        <div class="user-box">
            <input id="pass2" type="password" name="pass" required>
            <label>确认密码</label>
        </div>
        <a href="javascript:void(0)" onclick="subA()" >
            <span>周</span>
            <span>杰</span>
            <span>伦</span>
            <span>是</span>
            注册
        </a>
        <a href="/login.jsp" >
            <span>周</span>
            <span>杰</span>
            <span>伦</span>
            <span>是</span>
            返回登录
        </a>
    </form>
</div>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="/js/jquery-3.2.1.min.js"></script>
<script type="application/javascript">
    function subA() {
        if ($("#mobile").val().length != 11) {
            alert("电话号码不是11位");
            return false;
        }
        if ($("#pass1").val() != $("#pass2").val()) {
            alert("两次密码不相同");
            return false;
        }
        $("#regForm").submit();
    }
</script>
</body>
</html>