package com.spr.socialtv.service;

import com.spr.socialtv.dao.BoardDao;
import com.spr.socialtv.dto.BoardDto;
import com.spr.socialtv.entity.Board;
import com.spr.socialtv.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 생성 기능
    @Transactional
    public BoardDto createBoard(BoardDto boardDto) {
        BoardDao dao = new BoardDao();
        Board board = dao.convertToBoard(boardDto);
        Board result = boardRepository.save(board);
        return dao.ConvertToDto(result);
    }


    // 해당 게시판이 DB에 존재하는지 확인(이름)
    public Optional<Board> findBoard(String title) {
        return boardRepository.findByTitle(title);
    }

}
