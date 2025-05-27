<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cm.user.vo.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
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
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Chilanka&family=Montserrat:wght@300;400;500&display=swap" rel="stylesheet">
	<!-- <link href='../../resources/css/user/userMyPage.css' rel="stylesheet" type="text/css"> -->
	 <script>
        document.addEventListener('DOMContentLoaded', function () {
            var currentURI = '<%= request.getRequestURI() %>';
            var menuItems = document.querySelectorAll('.nav-item a');
            menuItems.forEach(function (item) {
                var href = item.getAttribute('href');
                if (currentURI.includes(href)) {
                    item.classList.add('active');
                }
            });
        });
    </script>
</head>
<body>
	<header>
    <div class="container py-2">
        <div class="row py-4 pb-0 pb-sm-4 align-items-center">
            <div class="col-sm-4 col-lg-3 text-center text-sm-start">
                <div class="main-logo">
                    <a href="/" style="display: flex; align-items: center;">
                        <img src="../../resources/images/집.png" alt="logo" class="img-fluid" style="height: 30px; width: 30px;">
                        <div style="font-size: 40px; padding-left: 15px; margin-top: 5px;">Share Life</div>
                    </a>
                </div>
            </div>
            <div style="padding-left: 50px;" class="col-sm-6 offset-sm-2 offset-md-0 col-lg-5 d-none d-lg-block">
                <div class="search-bar border rounded-2 px-3 border-dark-subtle">
                    <form id="search-form" class="text-center d-flex align-items-center" action="" method="">
                        <input type="text" class="form-control border-0 bg-transparent"
                               placeholder="통합검색" style="padding-left: 20px; padding-top: 23px;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                            <path fill="currentColor"
                                  d="M21.71 20.29L18 16.61A9 9 0 1 0 16.61 18l3.68 3.68a1 1 0 0 0 1.42 0a1 1 0 0 0 0-1.39ZM11 18a7 7 0 1 1 7-7a7 7 0 0 1-7 7Z"/>
                        </svg>
                    </form>
                </div>
            </div>
            <div class="col-sm-8 col-lg-4 d-flex justify-content-end gap-5 align-items-center mt-4 mt-sm-0 justify-content-center justify-content-sm-end"> 
     			<div class="support-box text-end d-none d-xl-block"> 
     				<a href="/user/userMyPage">
     				<span class="fs-6 secondary-font text-muted">마이페이지</span>
     				</a>
     			</div>
     			<div class="support-box text-end d-none d-xl-block"> 
     				<a href="/user/logout">
     				<span class="fs-6 secondary-font text-muted">로그아웃</span> 
     				</a>
     			</div>
     			<div class="support-box text-end d-none d-xl-block">
                    <a href="/customerCenter">
                        <span class="fs-6 secondary-font text-muted">고객센터</span>
                    </a>
                </div> 
     		</div>
     </div>
     </div>
     
       
    <div class="container-fluid">
        <hr class="m-0">
    </div>
    <div class="container">
        <nav class="main-menu d-flex navbar navbar-expand-lg ">
            <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar"
                    aria-controls="offcanvasNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                <div class="offcanvas-header justify-content-center">
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body justify-content-between" style="margin-left:20%; margin-right:20%;">
                    
                    <style>
                        .dropdown_icon {
                            margin-bottom: 5px;
                        }

                        .dropdown-menu {
                            font-size: 20px;
                        }
                        .nav-item {
                            padding-left: 10px;
                            font-size: 20px;
                        }
                    </style>
                    <ul class="navbar-nav menu-list list-unstyled d-flex gap-md-3 mb-0">
                                <!-- 메뉴 항목 -->
                                <li class="nav-item">
                                    <a href="/user/userMyPage" class="nav-link">프로필</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/user/userInfoChange" class="nav-link">회원정보수정</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/user/userLeave" class="nav-link">회원탈퇴</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/user/MyboardQboard" class="nav-link">내 게시글</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/user/MylikeQboard" class="nav-link">좋아요</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/user/myFlashmob" class="nav-link">내 모임</a>
                                </li>
                                <li class="nav-item">
                                    <a href="/myInquiryboard/list" class="nav-link">내 문의내역</a>
                                </li>
                     </ul>
                </div>
            </div>
        </nav>
    </div>
</header>