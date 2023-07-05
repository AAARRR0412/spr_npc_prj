package com.spr.socialtv.service;

import com.spr.socialtv.dto.*;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.entity.UserRoleEnum;
import com.spr.socialtv.jwt.JwtUtil;
import com.spr.socialtv.repository.UserRepository;
import com.spr.socialtv.security.UserDetailsImpl;
import com.spr.socialtv.util.redis.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private final HttpResponseDto responseDto;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       AuthenticationManagerBuilder authenticationManagerBuilder, RedisTemplate redisTemplate,
                       HttpResponseDto responseDto, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisTemplate = redisTemplate;
        this.responseDto = responseDto;
        this.mailSender = mailSender;
    }

    // Token 식별자

    public static final String BEARER_PREFIX = "Bearer ";


    // ADMIN_TOKEN

    private final String ADMIN_TOKEN = "7YOc7ZuI64uY7J2YIOq0gOumrOyekCDsvZTrk5zsnoXri4jri6QuIOy3qOq4ieyXkCDso7zsnZjtlZjsl6wg7KO87IS47JqULg==";


    // 회원가입

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
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        sendVerificationEmail(user);
        map.put("응답코드", 200);
        map.put("메시지", "회원가입을 위한 인증 메일을 발송했습니다.");
        return map;
    }

    // 인증 메일 발송

    private void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("회원 가입 인증 링크");
        message.setText("회원 가입 인증을 위해서 다음 링크를 클릭해주세요 : "
                + "http://localhost:8080/user/verify-email?token=" + user.getVerificationToken());
        mailSender.send(message);
    }

    // 인증 메일 확인

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new AuthenticationServiceException("유효하지 않은 인증 토큰입니다.");
        }
    }

    // 로그아웃

    public ResponseEntity<?> logout(String requestAccessToken) {
        if (StringUtils.hasText(requestAccessToken) && requestAccessToken.startsWith(BEARER_PREFIX)) {
            requestAccessToken =  requestAccessToken.substring(7);
        }

        // 1. Access Token 검증
        if (!jwtUtil.validateToken(requestAccessToken)) {
            return responseDto.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User 정보를 가져옵니다.
        Authentication authentication = jwtUtil.getAuthentication(requestAccessToken);

        // 3. Redis 에서 해당 User name으로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtUtil.getExpiration(requestAccessToken);
        redisTemplate.opsForValue()
                .set(requestAccessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return responseDto.success("로그아웃 되었습니다.");
    }

    // 재발급
    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtUtil.validateToken(reissue.getRefreshToken())) {
            return responseDto.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtUtil.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return responseDto.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return responseDto.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UserRoleEnum role = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getRole();
        TokenDto.TokenInfo tokenInfo = jwtUtil.generateToken(authentication, role);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return responseDto.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 Id를 찾을 수 없습니다. : " + userId));
        return convertToUserProfileDto(user);
    }

    private UserProfileDto convertToUserProfileDto(User user) {
        return new UserProfileDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
    @Transactional
    public UserResponseDto updateProfile(UserDetailsImpl userDetails, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        user.setUsername(userRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setSelfText(userRequestDto.getSelfText());

        userRepository.save(user);

        return new UserResponseDto(user);
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