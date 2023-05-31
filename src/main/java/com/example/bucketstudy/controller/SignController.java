package com.example.bucketstudy.controller;

import com.example.bucketstudy.domain.dto.SignRequest;
import com.example.bucketstudy.domain.dto.SignResponse;
import com.example.bucketstudy.repository.UserRepository;
import com.example.bucketstudy.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final UserRepository userRepository;
    private final SignService signService;

    @PostMapping(value = "/api/login")
    public ResponseEntity<SignResponse> sign(@RequestBody SignRequest request) throws Exception {
        SignResponse s = signService.login(request);
        System.out.println(s.getToken());
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PostMapping(value = "/api/register")
    public ResponseEntity<Boolean> signUp(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(signService.register(request), HttpStatus.OK);
    }

    @GetMapping(value = "/api/user/get")
    public ResponseEntity<SignResponse> getUser(@RequestParam String account) throws Exception {
        return new ResponseEntity<>(signService.getUser(account), HttpStatus.OK);
    }

    @GetMapping("/api/admin/get")
    public ResponseEntity<SignResponse> getAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>(signService.getUser(account), HttpStatus.OK);
    }
}
