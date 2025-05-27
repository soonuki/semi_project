<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
     <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="../../resources/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="../../resources/css/style.css">
    <link rel="stylesheet" type="text/css" href="../../resources/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="../../resources/css/paging.css">
        <link rel="stylesheet" type="text/css" href="../../resources/css/createSale.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Chilanka&family=Montserrat:wght@300;400;500&display=swap" rel="stylesheet">
    
</head>
<body>
	<%@ include file="../include/new_header.jsp" %>
    <!-- <img src='../../resources/img/free-icon-sale-7967061.png' id="sale_icon">판매나눔 -->
    <%@ page import ="com.cm.sale_share_board.vo.SaleShareList, java.util.*" %>
        <%@ page import="java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>
        <%@ page import="java.time.Duration, java.time.format.DateTimeFormatter" %>
        <% SaleShareList list = (SaleShareList)request.getAttribute("list");%>
        <% User u = (User)session.getAttribute("user"); %>
	<section id="section_id">
		<span class="user-nic">
   		<%= list.getUser_nic() %>
		</span>
		<% if(u != null){ %>
		<span class="chat-button">
    		<a href="/user/msgForm" id="chat_id">쪽지</a>
            <a href="<%= request.getContextPath() %>/sale_share_board/like?id=<%= list.getPost_no() %>" id="likeId" onclick="change(event)">좋아요</a>   
		</span>
		<%} %>
        <form action="/views/sale_share_board/createEnd" method="post" enctype="multipart/form-data" id="form_id" name="form_id">
            <div class="form_group">
                <img src="../../upload/<%= list.getImage_new_name() %>" width="200px" height="200px">
            </div>
            <div class="form_group">
                <p><label for="prod_id">상품명</label>
                <input type="text" id="prod_id" name="post_title" placeholder="text" value="<%= list.getPost_title() %>" disabled><br><br>
                <label for="price_id" id="price_label">가격</label>
                <input type="number" id="price_id" name="prod_price" value="<%= list.getProd_price() %>" disabled>
                <label>나눔</label>
                <input type="checkbox" id="free_checkBox" disabled><br><br>
                <label for="place" id="place_label" >거래장소</label>
                <input type="text" id="place" name="place" value="서울시 <%= list.getLocal_gu_name() %>" disabled><br>
                <br>
                <label>카테고리</label>
                <select name="prod_cate" disabled>
                    <option>
                    <% switch(list.getProd_cate()){
                    	case 1 : out.print("가구"); break;
                    	case 2 : out.print("식품"); break;
                    	case 3 : out.print("가전"); break;
                    	case 4 : out.print("의류"); break;
                    	default : out.print("기타"); 
                    	}%>
                    </option>
                </select>
                <br><br>
                 <label>거래상태</label>
                <select name="deal_status" disabled>
                	<option>
                    <% switch(list.getDeal_status()) {
                    	case 0 : out.print("거래중"); break;
                    	case 1 : out.print("예약중"); break;
                    	case 2 : out.print("거래완료"); break;
                    	}%>
                	</option>
                </select>
            </div>
            <div class="form_group">
                <div class="textarea-container">
                    <label for="description">설명</label>
                    <textarea placeholder="자세한 설명 글 올리기" id="description" name="post_text" disabled><%= list.getPost_title() %></textarea>
                </div>
            </div>
        </form>
        <!-- 뒤로가기 -->
        <input type="button" value="목록" onclick = "window.history.back();" style="float:right; margin-right :20px">
        <!-- 수정하기 -->
        <% int user_no = list.getUser_no(); %>
        
        <% if (u != null && u.getUser_no() == user_no) {%>
        <a href="<%= request.getContextPath() %>/sale_share_board/sale_share_board_edit?id=<%= list.getPost_no()%>"> 수정하기</a>
        <a href="<%= request.getContextPath() %>/sale_share_board/sale_share_board_delete?id=<%= list.getPost_no() %>">삭제하기</a>
        <a href="<%= request.getContextPath() %>/sale_share_board/sale_share_board_pull?id=<%=list.getPost_no() %>">끌어올리기</a>
        <%} else{ %>
        <%} %>
    </section>
    <script>
    document.getElementById("price_id").addEventListener("keyup", function() {
        var text_price = this.value;
        var free_checkBox = document.getElementById("free_checkBox");
        if (text_price == 0) {
            free_checkBox.checked = true;
        } else {
            free_checkBox.checked = false;
        }
    });
    </script>
		<script src="js/jquery-1.11.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
<script src="js/plugins.js"></script>
<script src="js/script.js"></script>
<script src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>
</body>
</html>