package com.example.server.global.auth.oauth2.model.socialLoader;

import com.example.server.domain.member.dto.MemberRequestDto;
import com.example.server.domain.member.dto.MemberResponseDto;
import com.example.server.domain.member.dto.MemberResponseDto.MemberTaskSuccessResponseDto;
import com.example.server.global.auth.oauth2.model.info.OAuth2UserInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

public abstract class SocialLoadStrategy {

    ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE  =  new ParameterizedTypeReference<>(){};

    protected final RestTemplate restTemplate = new RestTemplate();

    public OAuth2UserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        setHeaders(accessToken, headers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return sendRequestToSocialSite(request);//구체 클래스가 구현
    }

    protected abstract OAuth2UserInfo sendRequestToSocialSite(HttpEntity request);


    public void setHeaders(String accessToken, HttpHeaders headers) {
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public abstract void unlink(String memberId, String accessToken);
}
