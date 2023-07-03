package com.spr.socialtv.controller;

import com.spr.socialtv.dto.SignupRequestDto;
import com.spr.socialtv.dto.UserRequestDto;
import com.spr.socialtv.dto.UserResponseDto;
import com.spr.socialtv.security.UserDetailsImpl;
import com.spr.socialtv.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController  // 주소가 아닌 문자열로 그대로 리턴 가능함
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /*
     * 회원 가입
     * */
    @PostMapping("/signup")
    @ResponseBody
    public HashMap<String, Object> signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map =  userService.signup(signupRequestDto);

        return map;
    }


    // postman 으로 테스트 할때는 필요없지만 View 를 만든다는 가정하에 필요하다 생각하여 구현함
    @GetMapping("/get")
    public UserResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        String self_text = userDetails.getSelftext();
        UserResponseDto userResponseDto = new UserResponseDto(username, password, self_text);
        return userResponseDto;

    }

    @PutMapping("/update")
    public UserResponseDto updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto userResponseDto = userService.updateProfile(userDetails, userRequestDto);

        return userResponseDto;
    }



}
