<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>秒杀列表</title>
    <%@include file="common/common.jsp" %>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="icon" href="图片位置" type="image/x-icon"/>
</head>
<style>
    * {
        margin: 0;
        padding: 0;
    }
</style>
<body>
<div class="wrapper">
    <div class="container">
        <button class="btn btn-warning pull-right"><a href="/seckill/logout">退出登录</a></button>
        <div class="panel panel-danger">
            <div class="panel-heading">
                <span >秒杀列表</span>
            </div>
            <table class="table">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>库存</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>商品详情</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${items}" var="item">
                    <tr>
                        <td>${item.name}</td>
                        <td>${item.price}</td>
                        <td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a href="/seckill/detail/${item.id}">进入秒杀</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="wave"></div>
        </div>
    </div>
</div>
</body>
</html>
