package face2face.face2face.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;



@Data
@RequiredArgsConstructor
public class Record {
    //private int id;//自动增长
    private Interviewee interviewee;
    private Interviewer interviewer;
    private TimeInterval timeInterval;
    private String link;
    private String remark;
    private String result;
    private Integer index;
    private String score;
}
