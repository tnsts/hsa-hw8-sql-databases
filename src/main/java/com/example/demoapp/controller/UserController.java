package com.example.demoapp.controller;

import com.example.demoapp.service.UserGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserGeneratorService userGeneratorService;

    public UserController(UserGeneratorService userGeneratorService) {
        this.userGeneratorService = userGeneratorService;
    }

    @PostMapping("/users/generate")
    public ResponseEntity<Void> generateUsers(
            @RequestParam int amount,
            @RequestParam LocalDate birthdateFrom,
            @RequestParam LocalDate birthdayTo
    ) {
        userGeneratorService.generateAndSaveUsers(amount, birthdateFrom, birthdayTo);
        return ResponseEntity.ok().build();
    }
}
