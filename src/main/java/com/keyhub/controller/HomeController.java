package com.keyhub.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keyhub.dto.BoardDTO;
import com.keyhub.dto.ProductDTO;
import com.keyhub.service.BoardService;
import com.keyhub.service.MarketService;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    
    // 서비스 객체 준비
    private MarketService marketService = new MarketService();
    private BoardService boardService = new BoardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 최신 상품 4개 가져오기
        List<ProductDTO> recentProducts = marketService.getRecentProducts(4);
        request.setAttribute("recentProducts", recentProducts);
        
        // 2. 최신 게시글 5개 가져오기
        List<BoardDTO> recentBoards = boardService.getRecentBoards(5);
        request.setAttribute("recentBoards", recentBoards);

        // 3. 화면 이동
        String viewPath = "/WEB-INF/views/home/main.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}