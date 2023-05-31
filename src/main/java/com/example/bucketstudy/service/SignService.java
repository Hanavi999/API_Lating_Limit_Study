package com.example.bucketstudy.service;

import com.example.bucketstudy.domain.Authority;
import com.example.bucketstudy.domain.User;
import com.example.bucketstudy.domain.dto.SignRequest;
import com.example.bucketstudy.domain.dto.SignResponse;
import com.example.bucketstudy.repository.UserRepository;
import com.example.bucketstudy.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    // 로그인
    public SignResponse login(SignRequest request) throws Exception {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정정보입니다."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .pay(user.getPay())
                .nickname(user.getNickname())
                .roles(user.getRoles())
                .token(jwtProvider.createToken(user.getAccount(), user.getRoles()))
                .build();
    }


    // 회원가입
    public boolean register(SignRequest request) throws Exception {
        try {
            User user = User.builder()
                    .id(request.getId())
                    .account(request.getAccount())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .pay(request.getPay())
                    .build();

            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getUser(String account) throws Exception {
        User user = userRepository.findByAccount(account)
                .orElseThrow();
        return new SignResponse(user);
    }

}
