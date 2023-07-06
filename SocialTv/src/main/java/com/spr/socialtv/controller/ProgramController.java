package com.spr.socialtv.controller;

import com.spr.socialtv.entity.Program;
import com.spr.socialtv.service.ProgramService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


// 크롤링 데이터 테스트용도 컨트롤러
@Controller
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/news")
    public String news(Model model) throws Exception{
        List<Program> newsList = programService.getProgramData();
        model.addAttribute("news", newsList);

        return "news";
    }
}
