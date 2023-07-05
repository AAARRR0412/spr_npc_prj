package com.spr.socialtv.service;

import com.spr.socialtv.entity.Program;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService {
    private static String News_URL = "https://movie.daum.net/premovie/netflix?flag=Y";

    @PostConstruct
    public List<Program> getNewsDatas() throws IOException {
        List<Program> newsList = new ArrayList<>();
        Document document = Jsoup.connect(News_URL).timeout(5000).get();

        Elements contents = document.select("#mainContent > div > div.box_movie > ul > li");
        for (Element content : contents) {
            Program news = Program.builder()
                    .image(content.select("div:nth-child(1) > a > div > img").attr("abs:src")) // 이미지
                    .subject(content.select("div:nth-child(1) > a > div > img").attr("alt"))		// 제목
                    .url(content.select("div:nth-child(1) > a").attr("abs:href"))		// 링크
                    .build();
            newsList.add(news);
        }
        return newsList;
    }
}