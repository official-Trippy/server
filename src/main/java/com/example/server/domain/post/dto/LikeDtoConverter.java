package com.example.server.domain.post.dto;

import com.example.server.domain.image.domain.Image;
import com.example.server.domain.post.domain.Like;

import java.util.ArrayList;
import java.util.List;

public class LikeDtoConverter {
    public static LikeResponseDto.LikeBasicResponseDto convertToLikeBasicDto(Like like, Integer likeCount){
        return LikeResponseDto.LikeBasicResponseDto.builder()
                .likeId(like.getId())
                .postId(like.getPost().getId())
                .likeCount(likeCount)
                .nickName(like.getMember().getNickName())
                .memberId(like.getMember().getMemberId())
                .build();
    }

    public static LikeResponseDto.LikeListBasicResponseDto convertToLikeBasicListDto(List<Like> likeList, Integer likeCount, Long PostId){
        List<LikeResponseDto.LikeListDto> likeListDtoList = new ArrayList<>();
        for (Like like : likeList){
            Image profileImage = like.getMember().getImages().stream().filter(Image::isProfileImage).findAny().orElse(null);
            LikeResponseDto.LikeListDto likeDto = LikeResponseDto.LikeListDto.builder()
                    .likeId(like.getId())
                    .memberId(like.getMember().getMemberId())
                    .nickName(like.getMember().getNickName())
                    .blogName(like.getMember().getBlogName() != null ? like.getMember().getBlogName() : null)
                    .profileUrl(profileImage != null ? profileImage.getAccessUri() : null)
                    .build();
            likeListDtoList.add(likeDto);
        }
        return LikeResponseDto.LikeListBasicResponseDto.builder()
                .likeList(likeListDtoList)
                .likeCount(likeCount).postId(PostId)
                .build();

    }
}
