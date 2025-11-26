package com.keyhub.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtil {

    // 파일이 저장될 실제 물리적 경로 (C드라이브)
    private static final String UPLOAD_PATH = "C:\\keyhub_upload\\";

    // 업로드 처리 메서드
    public static Map<String, Object> parseRequest(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 저장소 폴더가 없으면 생성
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 2. 업로드 설정
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8"); // 한글 파일명 깨짐 방지

        try {
            // 3. 요청 파싱 (Form 태그 안의 모든 데이터를 가져옴)
            List<FileItem> items = upload.parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 3-1. 일반 텍스트 데이터 (제목, 가격 등)
                    // form-data는 한글 처리를 위해 UTF-8 변환 필요
                    resultMap.put(item.getFieldName(), item.getString("UTF-8"));
                } else {
                    // 3-2. 파일 데이터 (이미지)
                    if (item.getSize() > 0) {
                        // 파일명 중복 방지를 위해 UUID 사용 (예: abcd-1234_keyboard.jpg)
                        String originalName = item.getName();
                        String saveName = UUID.randomUUID().toString() + "_" + originalName;
                        
                        // 실제 저장
                        File storeFile = new File(UPLOAD_PATH + saveName);
                        item.write(storeFile);

                        // DB에 넣기 위해 파일명 Map에 담기
                        resultMap.put("imgUrl", saveName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}