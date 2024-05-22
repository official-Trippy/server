package com.example.server.domain.image.domain;

import com.example.server.domain.image.model.ImageType;
import com.example.server.domain.member.domain.Member;
//import com.example.server.domain.post.domain.Post;
//import com.example.server.domain.ticket.domain.Ticket;
import com.example.server.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Image")
public class Image {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;


    @Column(length = 1000)
    private String accessUri;

    private String imgUrl;

    private String authenticateId;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    public void setPost(Post post) {
        this.post = post;
    }

    public boolean isProfileImage() {
        return this.imageType == ImageType.PROFILE;
    }

    public boolean isBlogTitleImage() {
        return this.imageType == ImageType.BLOG;
    }


}
