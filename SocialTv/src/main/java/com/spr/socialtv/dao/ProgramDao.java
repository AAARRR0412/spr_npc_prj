package com.spr.socialtv.dao;

import com.spr.socialtv.dto.ProgramDto;
import com.spr.socialtv.entity.Program;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProgramDao {


    // entity -> dto 변환
    public ProgramDto ConvertToDto (Program program) {
        return ProgramDto.builder()
                .id(program.getId())
                .subject(program.getSubject())
                .image(program.getImage())
                .programType(program.getProgramType())
                .url(program.getUrl())
                .genre(program.getGenre())
                .openType(program.getOpenType())
                .madeBy(program.getMadeBy())
                .releaseDate(program.getReleaseDate())
                .build();
    }
}
