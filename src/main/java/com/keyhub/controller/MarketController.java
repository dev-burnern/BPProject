package com.keyhub.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keyhub.common.FileUploadUtil;
import com.keyhub.dto.MemberDTO;
import com.keyhub.dto.OrderDTO;
import com.keyhub.dto.PageInfoDTO; // PageInfoDTO import
import com.keyhub.dto.ProductDTO;
import com.keyhub.service.CartService;
import com.keyhub.service.MarketService;

@WebServlet("/market/*")
public class MarketController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private MarketService marketService = new MarketService();
    private CartService cartService = new CartService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        // 1. 목록 (GET) - 페이징 로직 추가됨
        if ("/list".equals(pathInfo) || pathInfo == null) {
            
            // --- 페이징 변수 초기화 ---
            int currentPage = 1;
            if (request.getParameter("cpage") != null) {
                currentPage = Integer.parseInt(request.getParameter("cpage"));
            }
            
            String keyword = request.getParameter("keyword");
            String category = request.getParameter("category");
            
            int pageLimit = 5;  // 하단 페이지 버튼 개수
            int boardLimit = 8; // 한 페이지당 보여줄 상품 개수
            int listCount = 0;  // 전체 상품 개수 (DB 조회 필요)

            // --- 1. 전체 개수 조회 (검색어/카테고리 유무에 따라 다름) ---
            // Service에 count 메서드가 구현되어 있다고 가정합니다.
            if (keyword != null && !keyword.trim().isEmpty()) {
                listCount = marketService.getSearchCount(keyword);
            } else {
                listCount = marketService.getListCount(category); // category가 null이면 전체 카운트
            }

            // --- 2. 페이징 계산 로직 ---
            int maxPage = (int) Math.ceil((double) listCount / boardLimit);
            int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
            int endPage = startPage + pageLimit - 1;
            if (endPage > maxPage) {
                endPage = maxPage;
            }

            PageInfoDTO pi = new PageInfoDTO(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
            
            // --- 3. 데이터 조회 (pi 객체 전달) ---
            List<ProductDTO> list;
            if (keyword != null && !keyword.trim().isEmpty()) {
                // Service의 search 메서드도 페이징 처리를 위해 pi를 받아야 함
                list = marketService.search(keyword, pi);
                request.setAttribute("searchKeyword", keyword); 
            } else {
                // Service의 getProductList 메서드도 pi를 받아야 함
                list = marketService.getProductList(category, pi);
                request.setAttribute("currentCategory", category);
            }
            
            request.setAttribute("pi", pi); // JSP에서 사용하기 위해 전달
            request.setAttribute("productList", list);
            request.getRequestDispatcher("/WEB-INF/views/market/list.jsp").forward(request, response);
        }
        
        // 2. 등록 화면 (GET)
        else if ("/write".equals(pathInfo) && "GET".equals(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/views/market/write.jsp").forward(request, response);
        }
        
        // 3. 등록 처리 (POST)
        else if ("/write".equals(pathInfo) && "POST".equals(request.getMethod())) {
            Map<String, Object> map = FileUploadUtil.parseRequest(request);
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            
            ProductDTO product = new ProductDTO();
            product.setTitle((String) map.get("title"));
            product.setContent((String) map.get("content"));
            product.setPrice(Integer.parseInt((String) map.get("price")));
            product.setCategory((String) map.get("category"));
            product.setSellerId(user.getId());
            product.setImgUrl(map.get("imgUrl") != null ? (String) map.get("imgUrl") : "no_image.png");

            marketService.registerProduct(product);
            response.sendRedirect(contextPath + "/market/list");
        }
        
        // 4. 상세 (GET)
        else if ("/detail".equals(pathInfo)) {
            String pNoStr = request.getParameter("pNo");
            if (pNoStr == null) { response.sendRedirect(contextPath + "/market/list"); return; }
            
            ProductDTO product = marketService.getProduct(Integer.parseInt(pNoStr));
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/market/detail.jsp").forward(request, response);
        }

        // 5. 장바구니 담기
        else if ("/cart/add".equals(pathInfo)) {
            int pNo = Integer.parseInt(request.getParameter("pNo"));
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            cartService.addCart(user.getId(), pNo);
            response.sendRedirect(contextPath + "/member/mypage");
        }
        
        // 6. 장바구니 삭제
        else if ("/cart/delete".equals(pathInfo)) {
            int cNo = Integer.parseInt(request.getParameter("cNo"));
            cartService.removeCart(cNo);
            response.sendRedirect(contextPath + "/member/mypage");
        }
        
        // 7. 상품 삭제
        else if ("/delete".equals(pathInfo)) {
            int pNo = Integer.parseInt(request.getParameter("pNo"));
            marketService.removeProduct(pNo);
            response.sendRedirect(contextPath + "/market/list");
        }
        
        // 8. 수정 화면
        else if ("/edit".equals(pathInfo) && "GET".equals(request.getMethod())) {
            int pNo = Integer.parseInt(request.getParameter("pNo"));
            ProductDTO product = marketService.getProduct(pNo);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/market/edit.jsp").forward(request, response);
        }
        
        // 9. 수정 처리
        else if ("/edit".equals(pathInfo) && "POST".equals(request.getMethod())) {
            Map<String, Object> map = FileUploadUtil.parseRequest(request);
            int pNo = Integer.parseInt((String) map.get("pNo"));
            
            ProductDTO product = new ProductDTO();
            product.setpNo(pNo);
            product.setTitle((String) map.get("title"));
            product.setContent((String) map.get("content"));
            product.setPrice(Integer.parseInt((String) map.get("price")));
            product.setCategory((String) map.get("category"));
            
            String newImg = (String) map.get("imgUrl");
            String oldImg = (String) map.get("originalImg");
            product.setImgUrl(newImg != null ? newImg : oldImg);
            
            marketService.modifyProduct(product);
            response.sendRedirect(contextPath + "/market/detail?pNo=" + pNo);
        }
        
        // 10. 결제 페이지
        else if ("/checkout".equals(pathInfo)) {
            int pNo = Integer.parseInt(request.getParameter("pNo"));
            ProductDTO product = marketService.getProduct(pNo);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/market/checkout.jsp").forward(request, response);
        }
        
        // 11. 결제 처리
        else if ("/buy".equals(pathInfo)) {
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            
            OrderDTO order = new OrderDTO();
            order.setMemberId(user.getId());
            order.setpNo(Integer.parseInt(request.getParameter("pNo")));
            order.setAmount(Integer.parseInt(request.getParameter("price")));
            order.setAddress(request.getParameter("address"));
            
            marketService.buyProduct(order);
            response.sendRedirect(contextPath + "/member/mypage");
        }
    }
}