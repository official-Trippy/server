package com.example.server.global.apiPayload.code.status;

import com.example.server.global.apiPayload.code.BaseErrorCode;
import com.example.server.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근 권한이 없는 요청입니다."),
    _LOGIN_FAILURE(HttpStatus.NOT_FOUND, "COMMON404", "요청 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _SERVICE_UNAVAILABLE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "COMMON503", "서버가 일시적으로 사용중지 되었습니다."),
    _JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "JSON 처리 중 오류가 발생하였습니다."),

    //유저 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "유저가 존재하지 않습니다."),
    MEMBER_EMAIL_PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "MEMBER4002", "이메일 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4003", "중복된 이메일입니다."),
    MEMBER_NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4004", "중복된 닉네임입니다."),
    MEMBER_FOLLOWING_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4005", "팔로우하려는 유저가 존재하지 않습니다."),
    MEMBER_FOLLOWING_MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4006", "이미 팔로우한 유저입니다."),
    MEMBER_FOLLOWING_MYSELF(HttpStatus.BAD_REQUEST, "MEMBER4007", "자기 자신을 팔로우할 수 없습니다."),
    MEMBER_BLOG_NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4008", "중복된 블로그명입니다."),
    MEMBER_AUTHORIZATION_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4009", "유효하지 않은 인증정보 입니다."),
    MEMBER_FOLLOW_MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4010", "대상 유저를 팔로우 또는 팔로잉 하지 않습니다."),
    MEMBER_ACCESS_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "MEMBER4011", "토큰 정보가 존재하지 않습니다."),
    MEMBER_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "MEMBER4012", "권한이 없는 사용자입니다."),
    MEMBER_ID_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4013", "중복된 멤버 아이디입니다."),
    MEMBER_COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "MEMBER4014", "쿠키 정보가 존재하지 않습니다."),
    MEMBER_LOGIN_NOT_SUPPORT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "MEMBER4015", "지원되지 않는 로그인 형식입니다."),
    MEMBER_CANNOT_ACCESS_FOLLOW(HttpStatus.BAD_REQUEST, "MEMBER4016", "해당 유저의 팔로우/팔로잉 목록에 접근할 수 없습니다."),
    MEMBER_PROFILE_ACCESS_DENY(HttpStatus.FORBIDDEN, "MEMBER4017", "프로필에 접근할 수 없는 유저입니다."),
    MEMBER_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4018", "유효하지 않은 토큰입니다."),
    MEMBER_SOCIAL_TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "MEMBER4019", "소셜 토큰이 제공되지 않았습니다."),
    MEMBER_EMAIL_AUTH_TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "MEMBER4020", "이메일 인증 토큰이 제공되지 않았습니다."),
    MEMBER_EMAIL_AUTH_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4021", "유효하지 않은 이메일 인증 토큰입니다."),
    MEMBER_INTEREST_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "MEMBER4022", "관심 분야 타입이 올바르지 않습니다."),



    //사진 응답
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMG4001", "사진 업로드에 실패하였습니다."),
    IMAGE_DOWNLOAD_FAIL(HttpStatus.NOT_FOUND, "IMG4002", "사진 다운로드에 실패하였습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMG4003", "사진 조회에 실패하였습니다."),
    IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "IMG4004", "사진 삭제에 실패하였습니다."),
    IMAGE_UPLOAD_TIME_COUNT_LIMIT(HttpStatus.BAD_REQUEST, "IMG4005", "사진 업로드 제한 횟수를 초과하였습니다."),

    // 신고 응답
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "REPORT4001", "신고내역이 존재하지 않습니다."),
    REPORT_TITLE_NULL(HttpStatus.BAD_REQUEST, "REPORT4002", "신고제목을 작성해주세요."),
    REPORT_CONTENT_NULL(HttpStatus.BAD_REQUEST, "REPORT4003", "신고내용을 작성해주세요."),

    // 소셜 로그인 응답
    SOCIAL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SOCIAL4001", "소셜 인증에 실패하였습니다."),
    KAKAO_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4002", "KAKAO 소셜 정보를 불러오는데에 실패하였습니다."),
    NAVER_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4003", "NAVER 소셜 정보를 불러오는데에 실패하였습니다."),
    GOOGLE_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4004", "GOOGLE 소셜 정보를 불러오는데에 실패하였습니다."),
    KAKAO_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5001", "KAKAO 소셜 연동 해제에 실패하였습니다."),
    NAVER_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5002", "NAVER 소셜 연동 해제에 실패하였습니다."),
    GOOGLE_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5003", "GOOGLE 소셜 연동 해제에 실패하였습니다."),

    // 게시물 응답
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST4001", "존재하지 않는 게시물입니다."),
    NO_PERMISSION__FOR_POST(HttpStatus.BAD_REQUEST, "POST4002", "해당 게시물에 권한이 없는 유저입니다."),
    TICKET_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST4003", "존재하지 않는 티켓입니다."),
    POST_TYPE_ERROR(HttpStatus.BAD_REQUEST, "POST4001", "게시물 타입이 아닙니다."),

    // OOTD 응답
    OOTD_TYPE_ERROR(HttpStatus.BAD_REQUEST, "OOTD4001", "OOTD 타입이 아닙니다."),
    OOTD_NOT_FOUND(HttpStatus.BAD_REQUEST, "OOTD4002", "존재하지 않는 OOTD 정보입니다."),
    NO_PERMISSION_NATION(HttpStatus.BAD_REQUEST, "OOTD4003", "날씨 정보를 불러올 수 없는 국가입니다."),
    ERROR_WHILE_GET_WEATHER(HttpStatus.BAD_REQUEST, "OOTD4004", "날씨 정보를 불러오는데에 실패하였습니다."),


    // 댓글 응답
    PARENT_COMMENT_AND_POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4001", "존재하지 않는 댓글이거나 부모 댓글이 게시글과 일치하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4002", "존재하지 않는 댓글입니다."),
    PARENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4003", "부모 댓글이 삭제되었기 때문에 대댓글을 달 수 없습니다."),
    COMMENT_IS_DELETED(HttpStatus.BAD_REQUEST, "COMMENT4004", "댓글이 삭제되었습니다."),
    COMMENT_CHILD_CANNOT_BE_PARENT(HttpStatus.BAD_REQUEST, "COMMENT4005", "대댓글은 부모 댓글이 될 수 없습니다."),
    COMMENT_MENTION_COMMENT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4006", "멘션할 댓글 정보가 존재하지 않습니다."),
    COMMNET_MENTION_MEMBER_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4007", "멘션 대상인 회원이 존재하지 않습니다."),

    // 좋아요 응답
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "LIKE4001", "이미 좋아요를 누른 게시물입니다."),
    POST_NOT_LIKED(HttpStatus.BAD_REQUEST, "LIKE4002", "좋아요를 누르지 않은 게시물입니다."),

    // FCM 응답
    FCM_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "FCM4001", "FCM 토큰이 존재하지 않습니다."),
    FCM_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "FCM4002", "FCM 권한이 없습니다."),

    // 북마크 응답
    BOOKMARK_TYPE_ERROR(HttpStatus.BAD_REQUEST, "BOOKMARK4001", "조회하려는 북마크 타입이 올바르지 않습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOOKMARK4002", "북마크가 존재하지 않습니다"),
    BOOKMARK_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "BOOKMARK4003", "이미 북마크한 게시물입니다."),
    BOOKMARK_NOT_EXIST(HttpStatus.BAD_REQUEST, "BOOKMARK4004", "북마크한 게시물이 존재하지 않습니다."),

    // 추천 응답
    INVALID_CITY_NAME(HttpStatus.BAD_REQUEST, "RECOMMEND4001", "주소에서 유효한 도시 이름을 찾을 수 없습니다."),

    // 시간 제한
    TIME_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"TIME4001", "시간 제한"),

    //푸시알림 응답
    NOTIFY_SEND_FAIL(HttpStatus.BAD_REQUEST,"NOTIFY4001", "알림 보내기를 실패하였습니다."),
    NOTIFY_UNSUPPORTED_TYPE(HttpStatus.BAD_REQUEST,"NOTIFY4002", "지원하지 않는 알림 타입입니다."),
    NOTIFY_NOT_FOUND(HttpStatus.BAD_REQUEST,"NOTIFY4003", "알림이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}