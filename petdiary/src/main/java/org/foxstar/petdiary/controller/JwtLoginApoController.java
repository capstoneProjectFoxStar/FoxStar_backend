package org.foxstar.petdiary.controller;

import lombok.RequiredArgsConstructor;
import org.foxstar.petdiary.domain.dto.JoinRequest;
import org.foxstar.petdiary.domain.dto.LoginRequest;
import org.foxstar.petdiary.oauth.JwtTokenUtil;
import org.foxstar.petdiary.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import foxstar.petdiary.auth.JwtTokenUtil;
import foxstar.petdiary.domain.dto.JoinRequest;
import foxstar.petdiary.domain.dto.LoginRequest;
import foxstar.petdiary.domain.entity.User;
import foxstar.petdiary.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-api-login")
public class JwtLoginApiController {

    private final UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {

        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            return "로그인 아이디가 중복됩니다.";
        }
        // 닉네임 중복 체크
        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            return "닉네임이 중복됩니다.";
        }
        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return"바밀번호가 일치하지 않습니다.";
        }

        userService.join2(joinRequest);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId : %s\nnickname : %s\nrole : %s",
                loginUser.getLoginId(), loginUser.getNickname(), loginUser.getRole().name());
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "관리자 페이지 접근 성공";
    }
}