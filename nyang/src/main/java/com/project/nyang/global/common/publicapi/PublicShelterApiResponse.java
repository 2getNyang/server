package com.project.nyang.global.common.publicapi;

import com.project.nyang.modules.shelter.dto.ShelterApiResponse;
import lombok.Data;

import java.util.List;

/**
 * 공공 API 응답을 매핑하는 PublicShelterApiResponse DTO클래스입니다.
 *
 * @author : 오승훈
 * @fileName : PublicShelterApiResponse
 * @since : 2025-07-16
 */
@Data
public class PublicShelterApiResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
    }

    @Data
    public static class Items {
        private List<ShelterApiResponse> item;
    }
}