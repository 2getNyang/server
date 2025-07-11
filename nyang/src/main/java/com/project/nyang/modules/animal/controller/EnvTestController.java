package com.project.nyang.modules.animal.controller;

/**
 * EnvTest
 *
 * @author : 선순주
 * @fileName : EnvTestController
 * @since : 2025-07-10
 */

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 이지은
 * @fileName : EnvTest
 * @since : 25. 7. 10.
 */
@RestController
@RequiredArgsConstructor
public class EnvTestController {

    @Value("${DB_SERVER:NOT_FOUND}")
    private String dbServer;

    @Value("${DB_PORT:NOT_FOUND}")
    private String dbPort;

    @GetMapping("/env")
    public String getEnv() {
        return "DB_SERVER=" + dbServer + ", DB_PORT=" + dbPort;
    }
}