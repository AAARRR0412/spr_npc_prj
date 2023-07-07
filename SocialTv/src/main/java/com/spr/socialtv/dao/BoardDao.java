package com.spr.socialtv.dao;

import com.spr.socialtv.dto.BoardDto;
import com.spr.socialtv.entity.Board;

public class BoardDao {


    // entity -> dto 변환
    public BoardDto ConvertToDto (Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .order(board.getOrder())
                .build();
    }

    public Board convertToBoard(BoardDto dto) {
        Board board = Board.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .order(dto.getOrder())
                .build();
        return board;
    }

}
