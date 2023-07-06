package com.spr.socialtv.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Entity
@Table(name = "program")
@Setter
@NoArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 프로그램 배급사 (기본 : Netflix)
    @Column(length = 20)
    private String programType;

    // 이미지
    @Column(length = 4000)
    private String image;

    // 타이틀
    @Column(length = 1000, nullable = false)
    private String subject;

    // 설명(동적으로 조회되어 현재 받아오는것은 안됨)
    @Column(length = 1000, nullable = true)
    private String description;

    // url
    @Column(length = 1000)
    private String url;

    // 장르
    @Column(length = 100)
    private String genre;
    // 러닝타임
    @Column(length = 100)
    private String runningTime;
    // 국가
    @Column(length = 80)
    private String madeBy;
    // 공개, 종료 여부
    
    @Column(length = 20)
    private String openType;
    // 공개, 종료 일자
    
    @Column(length = 20)
    private String releaseDate;

    @Builder
    public Program(String programType, String image, String subject, String description, String url, String genre, String runningTime, String madeBy, String openType, String releaseDate) {
        this.programType = programType;
        this.image = image;
        this.subject = subject;
        this.description = description;
        this.url = url;
        this.genre = genre;
        this.runningTime = runningTime;
        this.madeBy = madeBy;
        this.openType = openType;
        this.releaseDate = releaseDate;
    }
}
