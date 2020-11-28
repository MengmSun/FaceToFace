package face2face.face2face.entity;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Interviewee extends User{
    //一个面试者对应的面试官，可能有多个
    String id;
    List<Record> recordList;
    Interviewer interviewer;
    String email;
    String name;
    String CV;//简历的路径
    String status;
    Integer index;//面试轮数，初始化为0，每增添一次记录轮数增加一
}
