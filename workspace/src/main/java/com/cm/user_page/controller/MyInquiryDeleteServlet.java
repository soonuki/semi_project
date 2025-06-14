package com.cm.user_page.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cm.user_page.service.MyInquiryService;

@WebServlet("/user/deleteMyInquiry")
public class MyInquiryDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public MyInquiryDeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inquiryNoStr = request.getParameter("inquiryNo");
		int inquiryNo = Integer.parseInt(inquiryNoStr);
		
		int result = new MyInquiryService().deleteMyInquiry(inquiryNo);
		
		if(result > 0) {
			response.sendRedirect("/myInquiryboard/list");
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
