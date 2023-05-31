package com.example.bucketstudy.domain.dto;

import com.example.bucketstudy.domain.Authority;
import com.example.bucketstudy.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;

    private String account;

    private String password;

    private String nickname;

    private String pay;

    private List<Authority> roles = new ArrayList<>();

    private String token;

    public SignResponse(User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.nickname= user.getNickname();
        this.pay = user.getPay();
        this.roles = user.getRoles();
    }

}
