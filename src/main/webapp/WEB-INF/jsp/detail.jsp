<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>详情页</title>
    <%@include file="common/common.jsp" %>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        #seckillBox {
            background-color: #D00414;
            line-height: 120px;
            margin: auto auto;
            text-align: center;
        }

        .sec_time {
            color: #D00414;
            border-radius: 5px;
            border: 1px solid #440106;
            font-size: 45px;
            background-color: #440106;
        }

        .st {
            font-size: 18px;
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="common/top.jsp"/>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">${item.name}</h3>
        </div>
        <div class="alert alert-success" role="alert">数量：${item.number}-----生产日期：<fmt:formatDate
                value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
        <h1 id="seckillTitle" class="text-center">秒杀活动即将开始</h1>
        <div id="seckillBox">
            <div id="time">
                <span class="st">当前场次</span>
                <span id="sec_day" class="sec_time">00</span>
                <span>天</span>
                <span id="sec_hour" class="sec_time">00</span>
                <span>时</span>
                <span id="sec_min" class="sec_time">00</span>
                <span>分</span>
                <span id="sec_sec" class="sec_time">00</span>
                <span class="st">后开始抢购</span>
            </div>
        </div>
        <button id="snapButton" class="btn btn-warning disabled">点击抢购</button>
    </div>

    <script>
        //
        var startTime =${item.startTime.time};
        var endTime =${item.endTime.time};

        $.get("/seckill/now", function (result) {
            var serverNowLong = result['data'];
            var endLong = endTime - serverNowLong;
            //判断是否结束
            if (endLong <= 0) {
                endSeckill();
            } else {
                //没结束先判断是否开始
                var intervalID = window.setInterval(
                    function () {
                        var temp = startTime - serverNowLong;
                        //开始后关闭倒计时，开始秒杀倒计时
                        if (temp <= 0) {
                            clearInterval(intervalID);
                            startSecckill();
                            return;
                        }
                        var day = Math.floor(temp / 1000 / 60 / 60 / 24);
                        var hour = Math.floor(temp / 1000 / 60 / 60 % 24);
                        var min = Math.floor(temp / 1000 / 60 % 60);
                        var sec = Math.floor(temp / 1000 % 60);
                        $("#sec_day").text(day < 10 ? "0" + day : day);
                        $("#sec_hour").text(hour < 10 ? "0" + hour : hour);
                        $("#sec_min").text(min < 10 ? "0" + min : min);
                        $("#sec_sec").text(sec < 10 ? "0" + sec : sec);
                        serverNowLong += 1000;
                    }, 1000
                )
            }

        })

        function startSecckill() {
            var seckillId =${item.id};
            $.post("/seckill/getURL/" + seckillId, function (result) {
                //判断秒杀是否开启
                var seckillURL = result['data'];
                console.log(seckillURL['enable']);
                if (result && result['status'] && seckillURL['enable']) {
                    $("#seckillTitle").text("秒杀正在进行中！！！");
                    $("#snapButton").removeClass("btn-warning");
                    $("#snapButton").removeClass("disabled");
                    $("#snapButton").addClass("btn-success");
                    //添加一次点击事件
                    $("#snapButton").one('click', function () {
                        $(this).addClass("disabled");
                        window.location.href = "/seckill/execute/" + seckillId + "/" + seckillURL['md5'];
                    });
                    //秒杀结束倒计时
                    var serverNowLong = seckillURL['now'];
                    var endLong = seckillURL['end'];
                    var intervalID2 = window.setInterval(function () {
                            var temp = endLong - serverNowLong;
                            if (temp <= 0) {
                                clearInterval(intervalID2);
                                endSeckill();
                                return false;
                            }
                            var day = Math.floor(temp / 1000 / 60 / 60 / 24);
                            var hour = Math.floor(temp / 1000 / 60 / 60 % 24);
                            var min = Math.floor(temp / 1000 / 60 % 60);
                            var sec = Math.floor(temp / 1000 % 60);
                            $("#sec_day").text(day < 10 ? "0" + day : day);
                            $("#sec_hour").text(hour < 10 ? "0" + hour : hour);
                            $("#sec_min").text(min < 10 ? "0" + min : min);
                            $("#sec_sec").text(sec < 10 ? "0" + sec : sec);
                            serverNowLong += 1000;
                        }, 1000
                    )
                } else {
                    alert(result["message"]);
                    location.reload();
                }
            })
        }

        function endSeckill() {
            $("#seckillTitle").text("秒杀结束了！！");
            alert("活动已结束！");
            $("#snapButton").unbind('click');
            $("#snapButton").removeClass("btn-success");
            $("#snapButton").addClass("btn-warning");
            $("#snapButton").addClass("disabled");
        }

    </script>
</div>
</body>
</html>
