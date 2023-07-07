package com.spr.socialtv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardDto {
    private Long id;
    private String title;
    private int order;

    public BoardDto(String title, int order) {
        this.title = title;
        this.order = order;
    }
}
