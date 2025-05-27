package com.cm.user_page.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cm.user.service.UserService;
import com.cm.user.vo.User;


@WebServlet(name="userLeaveEnd",urlPatterns="/user/userLeaveEnd")
public class UserLeaveEndServler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UserLeaveEndServler() {
        super();
        
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String password = request.getParameter("password");

        if (user != null && password != null) {
            UserService us = new UserService();
            boolean isDeleted = us.deleteUser(user.getUser_id(), password);

            if (isDeleted) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/user_page/userLeave_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/user_page/userLeaveEnd.jsp?error=비밀번호가 일치하지 않습니다.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }
	

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
