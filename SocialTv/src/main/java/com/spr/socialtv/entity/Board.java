package com.spr.socialtv.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 게시판
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판명
    @Column(length = 20)
    private String title;

    //순서
    @Column 
    private int order;

    @Builder
    public Board(Long id,String title, int order) {
        this.id = id;
        this.title = title;
        this.order = order;
    }

}
