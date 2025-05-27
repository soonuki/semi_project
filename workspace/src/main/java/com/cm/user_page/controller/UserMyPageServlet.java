package com.cm.user_page.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cm.user.service.UserService;
import com.cm.user.vo.User;


@WebServlet("/user/userMyPage")
public class UserMyPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public UserMyPageServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        session.setAttribute("user", user);
        request.getRequestDispatcher("/views/user_page/userMyPage.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
