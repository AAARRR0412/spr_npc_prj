package com.spr.socialtv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProgramDto {

    private Long id;

    // 프로그램 배급사 (기본 : Netflix)
    private String programType;
    // 이미지
    private String image;
    // 타이틀
    private String subject;
    // 설명(동적으로 조회되어 현재 받아오는것은 안됨)
    private String description;
    // url
    private String url;
    // 장르
    private String genre;
    // 러닝타임
    private String runningTime;
    // 국가
    private String madeBy;
    // 공개, 종료 여부
    private String openType;
    // 공개, 종료 일자
    private String releaseDate;

}
