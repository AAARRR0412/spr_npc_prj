package com.spr.socialtv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
    private Long id;
    private String title;
    private int listOrder;

    @Builder
    public BoardDto(String title, int listOrder) {
        this.title = title;
        this.listOrder = listOrder;
    }
}
