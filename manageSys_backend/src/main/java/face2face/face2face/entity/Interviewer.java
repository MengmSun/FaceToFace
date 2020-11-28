package face2face.face2face.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Interviewer extends User{
    //一个面试官对应的面试者，可能有多个
    String id;
    List<Record> recordList;
    String email;
    String name;
    List<TimeInterval> freeTimeList;//面试官的空闲时间段

}
