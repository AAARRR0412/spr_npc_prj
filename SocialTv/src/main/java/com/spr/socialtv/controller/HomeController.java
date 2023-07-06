package com.spr.socialtv.controller;

import com.spr.socialtv.dto.ProgramDto;
import com.spr.socialtv.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 메인페이지
 * */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProgramService programService;

    // 메인 페이지
    @GetMapping("/")
    public String home(Model model) {
        List<ProgramDto> programList = programService.getProgramList();
        // JSONArray jsonList = new JSONArray();
        //ProgramDao dao = new ProgramDao();
        //jsonList = dao.ConvertListToJsonArray(programList);

        model.addAttribute("programList", programList);

        return "index";
    }
}
