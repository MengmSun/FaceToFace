package face2face.face2face.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TimeInterval {
    private String startTime;
    private String endTime;

    private Interviewer interviewer;
    private String t_user_id;

}
