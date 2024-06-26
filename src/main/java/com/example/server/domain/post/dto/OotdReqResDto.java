package com.example.server.domain.post.dto;

import com.example.server.domain.image.dto.ImageDto;
import com.example.server.domain.ticket.model.TicketColor;
import com.example.server.domain.ticket.model.Transport;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

public class OotdReqResDto {

    @Builder
    @Data
    public static class UploadOOTDRequestDto{
        private String area;
        private String weatherStatus;
        private String weatherTemp;
        private String detailLocation;
        private LocalDate date;
    }

    @Builder
    @Data
    public static class UpdateOOTDRequestDto{
        private Long id;
        private String area;
        private String weatherStatus;
        private String weatherTemp;
        private String detailLocation;
        private LocalDate date;
    }

    @Builder
    @Data
    public static class OotdBasicResponseDto{
        private Long id;
        private String area;
        private String weatherStatus;
        private String weatherTemp;
        private String detailLocation;
        private LocalDate date;
    }

    @Builder
    @Data
    public static class WeatherRequestDto{
        private double latitude;
        private double longitude;
        private String date;
    }

    @Builder
    @Data
    public static class WeatherResponseDto{
        private String area;
        private String avgTemp;
        private String maxTemp;
        private String minTemp;
        private String status;
    }
}
