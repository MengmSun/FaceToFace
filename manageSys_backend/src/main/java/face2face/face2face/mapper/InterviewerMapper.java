package face2face.face2face.mapper;

import face2face.face2face.entity.Interviewer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface InterviewerMapper {
    public Interviewer findById(String id);
    public Interviewer findById2(String id);
    public List<Interviewer> findAll(Map<String,Object> queryMap);
    public int countAll();
}
