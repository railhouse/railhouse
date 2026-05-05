package dev.jamius.weaver.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Hidden
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @GetMapping("/auth")
    @Operation(summary = "Test authentication")
    public String testAuth() {
        return "Authentication test successful";
    }
}
