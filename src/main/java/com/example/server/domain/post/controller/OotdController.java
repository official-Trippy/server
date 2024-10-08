package com.example.server.domain.post.controller;

import com.example.server.domain.post.dto.OotdReqResDto;
import com.example.server.domain.post.dto.PostRequestDto;
import com.example.server.domain.post.model.OrderType;
import com.example.server.domain.post.service.OotdService;
import com.example.server.global.apiPayload.ApiResponse;
import com.example.server.global.apiPayload.code.status.ErrorStatus;
import com.example.server.global.apiPayload.exception.handler.ErrorHandler;
import com.example.server.global.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ootd")
public class OotdController {

    private final OotdService ootdService;

    @PostMapping("")
    public ApiResponse<?> uploadOotdPost(@RequestBody PostRequestDto.UploadOOTDPostRequestDto requestDto) {
        String memberId = getLoginMemberId();
        requestDto.getPostRequest().setMemberId(memberId);
        log.info("OOTD 게시물 업로드 요청 : memberId = {}", memberId);
        return ApiResponse.onSuccess(ootdService.uploadOotdPost(requestDto));
    }



    @PatchMapping("")
    public  ApiResponse<?> updateOotd(@RequestBody OotdReqResDto.UpdateOOTDRequestDto updateOOTDRequestDto) {
        String memberId = getLoginMemberId();
        log.info("OOTD 게시물 업데이트 요청 : memberId = {}, ticketId = {}", memberId, updateOOTDRequestDto.getId() );
        return ApiResponse.onSuccess(ootdService.updateOotd(memberId, updateOOTDRequestDto));
    }

    @GetMapping("/info/{id}")
    public ApiResponse<?> getPost(@PathVariable("id") Long postId, HttpServletRequest request, HttpServletResponse response) {
        String memberId = getLoginMemberId();
        log.info("OOTD 게시물 조회 요청 : postId = {}, memberId = {}",postId,memberId );
        return ApiResponse.onSuccess(ootdService.getPost(postId,memberId,request,response));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllPost(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) OrderType orderType
    ) {
        if(page == null) page = 0;
        if(size==null) size = 0;
        if(orderType == null ) orderType = OrderType.LATEST;
        String memberId = getLoginMemberId();
        log.info("모든 OOTD 게시물 조회 요청 : memberId = {}, orderType = {}", memberId, orderType);
        return ApiResponse.onSuccess(ootdService.getAllPost(page, size,memberId,orderType));
    }

    @GetMapping("/my")
    public ApiResponse<?> getAllLoginMemberPost(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) OrderType orderType
    ) {
        if(page == null) page = 0;
        if(size==null) size = 0;
        String memberId = getLoginMemberId();
        if(orderType == null ) orderType = OrderType.LATEST;
        log.info("회원별 OOTD 게시물 조회 요청 : memberId = {}", memberId );
        return ApiResponse.onSuccess(ootdService.getAllMyPost(memberId, page, size, orderType));
    }

    @GetMapping("/by-member")
    public ApiResponse<?> getAllMemberPost(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam String memberId
    ) {
        if(page == null) page = 0;
        if(size==null) size = 0;
        if(orderType == null ) orderType = OrderType.LATEST;
        String loginMemberId = getLoginMemberId();
        log.info("회원별 게시물 조회 요청 : memberId = {}, loginMemberId = {}", memberId,loginMemberId );
        return ApiResponse.onSuccess(ootdService.getAllMemberPost(memberId,loginMemberId,page,size,orderType));
    }

    @GetMapping("/weather")
    public ApiResponse<?> getPost(@RequestParam double latitude,
                                  @RequestParam double longitude,
                                  @RequestParam String date) {
        log.info("OOTD 날씨 조회 요청");
        return ApiResponse.onSuccess(ootdService.callFlaskGetWeather(latitude, longitude, date));
    }


    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
