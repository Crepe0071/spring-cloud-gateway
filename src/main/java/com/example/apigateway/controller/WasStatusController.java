package com.example.apigateway.controller;

import com.example.apigateway.config.WasStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status/was")
public class WasStatusController {

    private final WasStateManager wasStateManager;

    @PostMapping("/up")
    public ResponseEntity<String> markUp(@RequestBody String wasAddress) {
        wasStateManager.markUp(wasAddress);
        System.out.println("WAS marked as UP: " + wasAddress);
        System.out.println("WAS marked as Ups: "+ wasStateManager.getUpInstances());
        System.out.println("WAS marked as Downs: "+ wasStateManager.getDownInstances());
        return ResponseEntity.ok("WAS marked as UP: " + wasAddress);
    }

    @PostMapping("/down")
    public ResponseEntity<String> markDown(@RequestBody String wasAddress) {
        wasStateManager.markDown(wasAddress);
        System.out.println("WAS marked as DOWN: " + wasAddress);
        System.out.println("WAS marked as Ups: "+ wasStateManager.getUpInstances());
        System.out.println("WAS marked as Downs: "+ wasStateManager.getDownInstances());
        return ResponseEntity.ok("WAS marked as DOWN: " + wasAddress);
    }
}

