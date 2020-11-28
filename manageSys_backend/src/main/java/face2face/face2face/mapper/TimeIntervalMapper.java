package face2face.face2face.mapper;

import face2face.face2face.entity.TimeInterval;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TimeIntervalMapper {
    public int add(TimeInterval timeInterval);
    public List<TimeInterval> findList(Map<String,Object> queryMap);
    public int delete(TimeInterval timeInterval);
    public int countList(Map<String,Object> queryMap);
}
