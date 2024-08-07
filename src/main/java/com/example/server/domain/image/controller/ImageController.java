package com.example.server.domain.image.controller;

import com.example.server.domain.image.dto.ImageDto;
import com.example.server.domain.image.service.ImageService;
import com.example.server.global.apiPayload.ApiResponse;
import com.example.server.global.apiPayload.code.status.ErrorStatus;
import com.example.server.global.apiPayload.exception.handler.ErrorHandler;
import com.example.server.global.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ApiResponse<?> uploadImage(@RequestPart(value="image", required = true) MultipartFile image, HttpSession session) throws Exception {
        return ApiResponse.onSuccess(imageService.uploadImg(image, getLoginMemberId(), session));
    }
    @DeleteMapping
    public ApiResponse<?> deleteImage(@RequestBody ImageDto imageDto) throws Exception{
        imageService.deleteImg(imageDto);
        return ApiResponse.onSuccess(null);
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
