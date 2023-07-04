package com.spr.socialtv.service;

import com.spr.socialtv.dto.SignupRequestDto;
import com.spr.socialtv.dto.UserDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.dto.UserProfileRequestDto;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.entity.UserRoleEnum;
import com.spr.socialtv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 userId를 찾을 수 없습니다. : " + userId));
        return convertToUserProfileDto(user);
    }

    private UserProfileDto convertToUserProfileDto(User user) {
        return new UserProfileDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public UserProfileDto updateUserProfile(Long userId, UserProfileRequestDto requestDto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 사용자 정보 업데이트
            user.setUsername(requestDto.getUsername());
            user.setEmail(requestDto.getEmail());
            // TODO: 필요한 다른 정보 업데이트 작업 수행

            User updatedUser = userRepository.save(user);
            return convertToUserProfileDto(updatedUser);
        }
        return null;
    }
}