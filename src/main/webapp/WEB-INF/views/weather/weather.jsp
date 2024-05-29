<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="cpath" value="${pageContext.request.contextPath}"/>
<c:set var="temper" value="${requestScope.T1H.obsrValue}" />

<!doctype html>
<html>


<head>
     <!--글꼴 설정입니다-->
     <link rel="preconnect" href="https://fonts.googleapis.com">
     <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
     <link href="https://fonts.googleapis.com/css2?family=Libre+Caslon+Text:ital
     ,wght@0,400;0,700;1,400&family=Moirai+One&display=swap" rel="stylesheet">

     <link rel = "stylesheet"
          href="${cpath}/resources/css/hello.css"
          type="text/css" />

    <title>Document</title>

     <script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>

     <link rel = "stylesheet"
           href="${cpath}/resources/css/weather.css"
           type="text/css" />

</head>


<body>
    <h1>weather folder</h1>
    <div class="weather-info">
    <div class="time-table">
        <span class="month">00</span>.
        <span class="date">00</span>.
        <span class="year">0000</span>.
        <span class="todayTimes">00000</span>
    </div>
        <br>
        <hr>
        <br>

    <!--https://www.weather.go.kr/w/resources/pdf/dongnaeforecast_rss.pdf  날씨데이터 종류와 수치 종류 정리-->
    <h1>오늘의 날씨</h1>


        <br>

        <div class="weather-status">
            <c:choose>

             <c:when test="${SKY.obsrValue eq 1}">
                <img src="${cpath}/resources/WeatherIcon/img.png" alt="맑은 날씨 이미지">
             </c:when>

             <c:when test="${SKY.obsrValue eq 3}">
                <img src="${cpath}/resources/WeatherIcon/img_1.png" alt="조금 흐린 날씨 이미지">
             </c:when>

             <c:when test="${SKY.obsrValue eq 4}">
                <img src="${cpath}/resources/WeatherIcon/img_2.png" alt="조금 흐린 날씨 이미지">
             </c:when>

             <c:when test="${SKY.obsrValue eq 6}">
                <img src="${cpath}/resources/WeahtherIcon/img2.png" alt="많이 흐린 날씨 이미지">
             </c:when>

             <c:when test="${PTY.obsrValue eq 1}">
                <img src="${cpath}/resources/WeatherIcon/img_3.png" alt="비오는 날씨 이미지">
             </c:when>

             <c:otherwise>
                <p>날씨 정보를 가져오지 못했습니다.</p>
             </c:otherwise>

            </c:choose>
            <div class="information">
                온도 : ${T1H.obsrValue}°C
                <hr>
                습도 : ${REH.obsrValue}
            </div>
        </div>
    </div>

    <script>

        setInterval(myTimer, 1000); // 1초마다 호출되게 한다.


        function myTimer() {
            let today = new Date(); //데이트객체생성
            let y = today.getFullYear();
            let m = today.getMonth() + 1; //0부터 시작하므로 +1을 더해야 현재 월 이 된다.
            let d = today.getDate(); //일 값을 받아낸다.
            let day = today.getDay(); // 요일의 값을 받아낸다.
            let weekday = new Array(7); // 어레이의 각 요일을 할당하고
            weekday[0] = "Sunday";
            weekday[1] = "Monday";
            weekday[2] = "Tuesday";
            weekday[3] = "Wednesday";
            weekday[4] = "Thursday";
            weekday[5] = "Friday";
            weekday[6] = "Saturday";

            let t_time = today.toLocaleTimeString();

            $(".time-table .year").text(y);
            $(".time-table .month").text(m);
            $(".time-table .date").text(d);
            $(".day").text(weekday[today.getDay()]); //어레이의 요일의 값을 할당해 요일 출력
            $(".time-table .todayTimes").text(t_time);
        }

    </script>
 </body>


</html>