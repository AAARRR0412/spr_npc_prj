package com.spr.socialtv.dao;

import com.spr.socialtv.dto.ProgramDto;
import com.spr.socialtv.entity.Program;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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

    // 화면에 리턴
    public JSONArray ConvertListToJsonArray(List<ProgramDto> programList) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0 ; i < programList.size(); i ++) {
            JSONObject tmp = new JSONObject();
            tmp.put("id", programList.get(i).getId());
            tmp.put("programType", programList.get(i).getProgramType());
            tmp.put("image", "test");
            tmp.put("subject", programList.get(i).getSubject());
            tmp.put("description", "");
            tmp.put("url", programList.get(i).getUrl());
            tmp.put("genre", programList.get(i).getGenre());
            tmp.put("runningTime", programList.get(i).getRunningTime());
            tmp.put("madeBy", programList.get(i).getMadeBy());
            tmp.put("openType", programList.get(i).getOpenType());
            tmp.put("releaseDate", programList.get(i).getReleaseDate());
            jsonArray.put(i, tmp);
        }
        return jsonArray;
    }

}
