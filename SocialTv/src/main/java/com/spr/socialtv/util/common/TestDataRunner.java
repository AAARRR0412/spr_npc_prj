
package com.spr.socialtv.util.common;


import com.spr.socialtv.dto.BoardDto;
import com.spr.socialtv.entity.Board;
import com.spr.socialtv.repository.BoardRepository;
import com.spr.socialtv.repository.UserRepository;
import com.spr.socialtv.service.BoardService;
import com.spr.socialtv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestDataRunner implements ApplicationRunner {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;


    @Override
    public void run(ApplicationArguments args) {
        // 테스트 생성
        BoardDto boardDto = new BoardDto("자유게시판", 1);
        Optional<Board> board = boardService.findBoard("자유게시판");
        if (!board.isPresent() || board.isEmpty()) {
            boardService.createBoard(boardDto);
        }

        //테스트 생성
        boardDto = new BoardDto("프로그램게시판", 2);
        board = boardService.findBoard("프로그램게시판");
        if (!board.isPresent() || board.isEmpty()) {
            boardService.createBoard(boardDto);
        }

        // 테스트 User 생성
        //User testUser = new User("Robbie", passwordEncoder.encode("1234"), "robbie@sparta.com", UserRoleEnum.USER);
        // testUser = userRepository.save(testUser);
    }
}

