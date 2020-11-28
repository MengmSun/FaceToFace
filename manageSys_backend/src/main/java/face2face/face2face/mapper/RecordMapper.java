package face2face.face2face.mapper;

import face2face.face2face.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RecordMapper {
    public List<Record> findList(Map<String,Object> queryMap);
    public int countList(Map<String,Object> queryMap);
    public List<Record> findOnGoingList(Map<String,Object> queryMap);
    public int countOnGoingList(Map<String,Object> queryMap);
    public List<Record> findUndecidedList(Map<String,Object> queryMap);
    public int countUndecidedList();
    public List<Record> findDecidedList(Map<String,Object> queryMap);
    public int countDecidedList();
    public int add(Record record);
    public int updateRemark(Record record);
    public int updateResult(Record record);
    public int updateLink(Record record);
}
