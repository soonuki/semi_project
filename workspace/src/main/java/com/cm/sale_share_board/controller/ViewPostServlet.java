package com.cm.sale_share_board.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cm.sale_share_board.service.SaleShareBoardService;
import com.cm.sale_share_board.vo.SaleShareList;


// 상세글 조회하기
@WebServlet("/sale_share_board/sale_share_board_detail")
public class ViewPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ViewPostServlet() {
        super();
      
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 int i = 0;
		String post = request.getParameter("id");
		int postNo = Integer.parseInt(post);
		System.out.println("번호값 : "+postNo);
		SaleShareList ssl = new SaleShareBoardService().selectBoard(postNo);
		request.setAttribute("list", ssl);
		
		RequestDispatcher view = request.getRequestDispatcher("/views/sale_share_board/sale_share_board_detail.jsp");
		view.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

