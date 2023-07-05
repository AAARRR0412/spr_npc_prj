package com.spr.socialtv.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Program {
    private String image;
    private String subject;
    private String url;
}
