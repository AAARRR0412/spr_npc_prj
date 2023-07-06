package com.spr.socialtv.service;

import com.spr.socialtv.dao.ProgramDao;
import com.spr.socialtv.dto.ProgramDto;
import com.spr.socialtv.entity.Program;
import com.spr.socialtv.repository.ProgramRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private static String News_URL = "https://movie.daum.net/premovie/netflix?flag=Y";

    private final ProgramRepository programRepository;

    private static final String PROGRAM_TYPE = "Netflix";


    // db에서 가져오기
    @Transactional
    public List<ProgramDto> getProgramList() {
        List<Program> programList = programRepository.findAll();
        List<ProgramDto> dtoList = new ArrayList<>();
        ProgramDao dao = new ProgramDao();
        for (Program program : programList) {
            ProgramDto dto = dao.ConvertToDto(program);
            dtoList.add(dto);
        }
        return dtoList;
    }


    // 외부에서 프로그램 데이터 목록 가져오기
    @PostConstruct
    public List<Program> getProgramData() throws IOException {
        List<Program> newsList = new ArrayList<>();
        Document document = Jsoup.connect(News_URL).timeout(5000).get();

        Elements contents = document.select("#mainContent > div > div.box_movie > ul > li");
        for (Element content : contents) {
            String urls = content.select("div:nth-child(1) > a").attr("abs:href");
            Document detailSite = Jsoup.connect(urls).timeout(5000).get();
            Elements detailContents = detailSite.select("#mainContent > div > div.box_basic");

            long tmpStandard = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl").stream().count();
            String tmpGenre = "";
            String tmpMadeBy = "";
            String runningTime = "";
            // 임의의 순서대로 등록하므로 하드코딩 방식으로 구현
            if (tmpStandard == 3) {
                if (detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(1) > dt").text().equals("공개")) {
                    tmpGenre = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(2) > dd").text();
                    tmpMadeBy = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(3) > dd").text();
                    runningTime = "";
                } else {
                    tmpGenre = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(1) > dd").text();
                    tmpMadeBy = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(2) > dd").text();
                    runningTime = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(3) > dd").text();
                }
            } else if (tmpStandard == 4) {
                tmpGenre = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(1) > dd").text();
                tmpMadeBy = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(2) > dd").text();
                runningTime = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(4) > dd").text();
            } else if (tmpStandard == 5) {
                tmpGenre = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(2) > dd").text();
                tmpMadeBy = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(3) > dd").text();
                runningTime = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(5) > dd").text();
            } else if (tmpStandard == 6) {
                tmpGenre = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(3) > dd").text();
                tmpMadeBy = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(4) > dd").text();
                runningTime = detailContents.select("div.info_detail > div.detail_cont > div:nth-child(1) > dl:nth-child(6) > dd").text();
            } else {
                tmpGenre = "";
                tmpMadeBy = "";
                runningTime = "";
            }

            Program news = Program.builder()
                    .image(content.select("div:nth-child(1) > a > div > img").attr("abs:src")) // 이미지
                    .subject(content.select("div:nth-child(1) > a > div > img").attr("alt"))		// 제목
                    .openType(content.select("div:nth-child(1) > div > span").html().trim().substring(0,2)) // 공개, 종료 예정
                    .releaseDate(content.select("div:nth-child(1) > div > span > span").text()) // 공개, 종료 일자
                    .url(content.select("div:nth-child(1) > a").attr("abs:href"))		// 링크
                    .genre(tmpGenre) // 장르
                    .madeBy(tmpMadeBy)// 국가
                    .runningTime(runningTime) //러닝타임
                    .programType(PROGRAM_TYPE) // 현재 넷플릭스 데이터를 받아오는중
                    .build();
            newsList.add(news);
        }
        return newsList;
    }


    @Transactional
    public void save(Program program) {
        programRepository.save(program);
    }


    @Transactional
    public void update(Long id, ProgramDto programDto) {
        Program program  = programRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        program.setGenre(programDto.getGenre());
        program.setUrl(programDto.getUrl());
        program.setReleaseDate(programDto.getReleaseDate());
        program.setOpenType(programDto.getOpenType());
        programRepository.save(program);
    }
}