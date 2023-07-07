package com.spr.socialtv.dao;

import com.spr.socialtv.dto.BoardDto;
import com.spr.socialtv.entity.Board;

public class BoardDao {


    // entity -> dto 변환
    public BoardDto ConvertToDto (Board board) {
        return BoardDto.builder()
                .title(board.getTitle())
                .listOrder(board.getListOrder())
                .build();
    }

    public Board convertToBoard(BoardDto dto) {
        Board board = Board.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .listOrder(dto.getListOrder())
                .build();
        return board;
    }

}
