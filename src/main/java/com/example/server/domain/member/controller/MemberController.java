package com.example.server.domain.member.controller;


import com.example.server.domain.member.domain.Member;
import com.example.server.domain.member.dto.MemberRequestDto;
import com.example.server.domain.member.dto.MemberRequestDto.CommonCreateMemberRequestDto;
import com.example.server.domain.member.dto.MemberRequestDto.CreateMemberRequestDto;
import com.example.server.domain.member.dto.MemberResponseDto;
import com.example.server.domain.member.repository.MemberRepository;
import com.example.server.domain.member.service.MemberService;
import com.example.server.global.apiPayload.ApiResponse;
import com.example.server.global.apiPayload.code.status.ErrorStatus;
import com.example.server.global.apiPayload.exception.handler.ErrorHandler;
import com.example.server.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.server.domain.member.dto.MemberResponseDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public ApiResponse<?> signUp(@RequestBody CreateMemberRequestDto createMemberRequestDto) {
        log.info("회원가입 요청 : memberId = {}", createMemberRequestDto.getMemberId());
        return ApiResponse.onSuccess(memberService.signUp(createMemberRequestDto));
    }

    @PostMapping("/signup/common")
    public ApiResponse<?> commonSignUp(@RequestBody CommonCreateMemberRequestDto commonCreateMemberRequestDto) {
        String loginMemberId = getLoginMemberId();
        return ApiResponse.onSuccess(memberService.commonSignUp(commonCreateMemberRequestDto, loginMemberId));
    }


    @GetMapping
    public ApiResponse<?> getMyInfo() {
        String memberId = getLoginMemberId();
        return ApiResponse.onSuccess(memberService.getMyInfo(memberId));
    }

    @GetMapping("/isNewMember")
    public ApiResponse<?> isNewMember() {
        String memberId = getLoginMemberId();
        return ApiResponse.onSuccess(memberService.isNewMember(memberId));
    }

    @GetMapping("/isDuplicated")
    public ApiResponse<?> isDuplicated(@RequestParam(value = "memberId", required = false) String memberId,
                                                     @RequestParam(value = "email", required = false) String email,
                                                     @RequestParam(value = "nickName", required = false) String nickName,
                                                     @RequestParam(value = "blogName", required = false) String blogName) throws Exception {
        IsDuplicatedDto isDuplicatedDto;
        String ALREADY_EXIST_MESSAGE = "이미 가입된 내역이 존재합니다. 가입된 로그인 플랫폼 : ";

        if (memberId != null ) {
            String message = ALREADY_EXIST_MESSAGE + memberService.getSocialTypeByMemberId(email);
            isDuplicatedDto = IsDuplicatedDto.builder()
                    .isDuplicated(memberService.isExistByMemberId(memberId))
                    .message(memberService.isExistByMemberId(memberId)? message : "사용 가능한 아이디입니다.")
                    .build();
        } else if (email != null) {
            String message = ALREADY_EXIST_MESSAGE + memberService.getSocialTypeByEmail(email);
            isDuplicatedDto = IsDuplicatedDto.builder()
                    .isDuplicated(memberService.isExistByEmail(email))
                    .message(memberService.isExistByEmail(email)? message : "사용 가능한 이메일입니다.")
                    .build();
        } else if (nickName != null) {
            isDuplicatedDto = IsDuplicatedDto.builder()
                    .isDuplicated(memberService.isExistByNickName(nickName))
                    .message(memberService.isExistByNickName(nickName) ? ErrorStatus.MEMBER_NICKNAME_ALREADY_EXIST.getMessage()
                            : "사용 가능한 닉네임입니다.")
                    .build();
        } else if (blogName != null) {
            isDuplicatedDto = IsDuplicatedDto.builder()
                    .isDuplicated(memberService.isExistByBlogName(blogName))
                    .message(memberService.isExistByBlogName(blogName) ? ErrorStatus.MEMBER_BLOG_NAME_ALREADY_EXIST.getMessage()
                            : "사용 가능한 블로그 이름입니다.")
                    .build();
        } else {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(),
                    "요청 파라미터가 잘못되었습니다.");
        }
        return ApiResponse.onSuccess(isDuplicatedDto);

    }

    @PostMapping("/follow")
    public ApiResponse<?> followMember(@RequestParam(value = "memberId", required = false) String followingMemberId) {
        String memberId = getLoginMemberId();
        return ApiResponse.onSuccess(memberService.followMember(memberId, followingMemberId));
    }

    @GetMapping("/follow")
    public ApiResponse<?> getFollow(@RequestParam(value = "type") String type)  {
        // 비활성화된 멤버는 조회 안되게 하는 로직 추가 구현 해야함
        String memberId = getLoginMemberId();
        if (type.equals("follower")) {
            return ApiResponse.onSuccess(memberService.getFollowerList(memberId));
        } else if (type.equals("following")) {
            return ApiResponse.onSuccess(memberService.getFollowingList(memberId));
        }

        return ApiResponse.onSuccess(ErrorStatus._BAD_REQUEST);
    }

    @DeleteMapping("/follow")
    public ApiResponse<?> deleteFollow(@RequestParam(value = "type") String type,
                                      @RequestParam(value = "followingMemberId") String followingMemberId) {
        String memberId = getLoginMemberId();
        if (type.equals("follower")) {
            return ApiResponse.onSuccess(memberService.deleteFollower(memberId, followingMemberId));
        }
        else if (type.equals("following")) {
            return ApiResponse.onSuccess(memberService.unFollow(memberId, followingMemberId));
        }
        return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(),
                "type 형식이 잘못되었습니다.");
    }

    @PatchMapping("/password")
    public ApiResponse<?> changePassword(@RequestBody MemberRequestDto.ChangePasswordRequestDto requestDto,
                                         @RequestParam(value = "code") String code) {
        return ApiResponse.onSuccess(memberService.changePassword(requestDto, code));
    }


    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }



}
