package com.spr.socialtv.service;

import com.spr.socialtv.dto.SignupRequestDto;
import com.spr.socialtv.dto.UserRequestDto;
import com.spr.socialtv.dto.UserResponseDto;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.entity.UserRoleEnum;
import com.spr.socialtv.repository.UserRepository;
import com.spr.socialtv.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "7YOc7ZuI64uY7J2YIOq0gOumrOyekCDsvZTrk5zsnoXri4jri6QuIOy3qOq4ieyXkCDso7zsnZjtlZjsl6wg7KO87IS47JqULg==";


    /*
     * 회원가입
     * */
    public HashMap<String, Object> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        HashMap<String, Object> map = new HashMap<>();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            map.put("응답코드", 401);
            map.put("메시지", "중복된 사용자가 존재합니다.");
            return map;
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            map.put("응답코드", 401);
            map.put("메시지", "중복된 Email 입니다.");
            return map;
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                map.put("응답코드", 401);
                map.put("메시지", "관리자 암호가 틀려 등록이 불가능합니다.");
                return map;
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
        map.put("응답코드", 200);
        map.put("메시지", "회원가입을 성공하였습니다.");
        return map;
    }


    @Transactional
    public UserResponseDto updateProfile(UserDetailsImpl userDetails, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        user.setUsername(userRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setSelf_text(userRequestDto.getSelf_text());

        userRepository.save(user);

        return new UserResponseDto(user);
    }


}