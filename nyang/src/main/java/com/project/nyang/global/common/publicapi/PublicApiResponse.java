package com.project.nyang.global.common.publicapi;

import com.project.nyang.modules.animal.dto.AnimalApiResponse;
import lombok.Data;

import java.util.List;

/**
 * 공공 API 응답을 매핑하는 PublicApiResponse DTO클래스입니다.
 *
 * @author : 엄아영
 * @fileName : PublicApiResponse
 * @since : 2025-07-14
 */

@Data
public class PublicApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMessage;
    }

    @Data
    public static class Body {
        private Items items;
    }

    @Data
    public static class Items {
        private List<AnimalApiResponse> item;
    }
}