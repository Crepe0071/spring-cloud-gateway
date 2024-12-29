package com.example.apigateway.controller;

import com.example.apigateway.config.WasStateManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/was-status")
public class WasStatusController {

    private final WasStateManager wasStateManager;

    public WasStatusController(WasStateManager wasStateManager) {
        this.wasStateManager = wasStateManager;
    }

    @PostMapping("/up")
    public ResponseEntity<String> markUp(@RequestBody String wasAddress) {
        wasStateManager.markUp(wasAddress);
        return ResponseEntity.ok("WAS marked as UP: " + wasAddress);
    }

    @PostMapping("/down")
    public ResponseEntity<String> markDown(@RequestBody String wasAddress) {
        wasStateManager.markDown(wasAddress);
        return ResponseEntity.ok("WAS marked as DOWN: " + wasAddress);
    }
}

