package com.keyhub.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 사용법: <img src="/KeyHub/image?name=파일명.jpg">
@WebServlet("/image")
public class ImageController extends HttpServlet {

    private static final String UPLOAD_PATH = "C:\\keyhub_upload\\";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String fileName = request.getParameter("name");
        if (fileName == null) return;

        File file = new File(UPLOAD_PATH + fileName);
        if (!file.exists()) return;

        // 이미지 타입 설정
        response.setContentType("image/jpeg"); 
        
        // 파일을 읽어서 응답 스트림으로 전송 (스트리밍)
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            
            byte[] buffer = new byte[1024 * 8]; // 8KB 버퍼
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
    }
}