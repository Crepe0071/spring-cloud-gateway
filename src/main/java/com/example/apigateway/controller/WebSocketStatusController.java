package com.example.apigateway.controller;

import com.example.apigateway.config.WebSocketStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status/web-socket")
public class WebSocketStatusController {

    private final WebSocketStateManager webSocketStateManager;

    @PostMapping("/up")
    public ResponseEntity<String> markUp(@RequestBody String webSocketAddress) {
        webSocketStateManager.markUp(webSocketAddress);
        System.out.println("WEB-SOCKET marked as UP: " + webSocketAddress);
        System.out.println("WEB-SOCKET marked as Ups: "+ webSocketStateManager.getUpInstances());
        System.out.println("WEB-SOCKET marked as Downs: "+ webSocketStateManager.getDownInstances());
        return ResponseEntity.ok("WEB-SOCKET marked as UP: " + webSocketAddress);
    }

    @PostMapping("/down")
    public ResponseEntity<String> markDown(@RequestBody String webSocketAddress) {
        webSocketStateManager.markDown(webSocketAddress);
        System.out.println("WEB-SOCKET marked as DOWN: " + webSocketAddress);
        System.out.println("WEB-SOCKET marked as Ups: "+ webSocketStateManager.getUpInstances());
        System.out.println("WEB-SOCKET marked as Downs: "+ webSocketStateManager.getDownInstances());
        return ResponseEntity.ok("WEB-SOCKET marked as DOWN: " + webSocketAddress);
    }
}

