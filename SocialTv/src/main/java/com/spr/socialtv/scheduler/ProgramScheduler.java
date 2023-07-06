package com.spr.socialtv.scheduler;

import com.spr.socialtv.dao.ProgramDao;
import com.spr.socialtv.dto.ProgramDto;
import com.spr.socialtv.entity.Program;
import com.spr.socialtv.repository.ProgramRepository;
import com.spr.socialtv.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "Program Scheduler")
@Component
@RequiredArgsConstructor
public class ProgramScheduler {

    private final ProgramService programService;
    private final ProgramRepository programRepository;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시
    //@Scheduled(cron = "*/10 * * * * *") //10 초마다
    public void insertPrograms() throws InterruptedException {
        log.info("ott 프로그램 등록 실행");
        try {
            List<Program> programList = programService.getProgramData();
            for (int i = 0; i < programList.size(); i++) {
                Program tmpProgram = programList.get(i);
                Optional<Program> prevData = programRepository.findBySubjectAndReleaseDate(tmpProgram.getSubject(), tmpProgram.getReleaseDate());
                if (!prevData.isPresent()) {
                    programService.save(tmpProgram);
                } else {
                    Program prevDataVo = prevData.get();
                    ProgramDao dao = new ProgramDao();
                    ProgramDto dto =  dao.ConvertToDto(tmpProgram);
                    programService.update(prevDataVo.getId(), dto);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
