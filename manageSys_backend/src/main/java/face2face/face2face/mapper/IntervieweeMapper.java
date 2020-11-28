package face2face.face2face.mapper;

import face2face.face2face.entity.Interviewee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface IntervieweeMapper {
    public List<Interviewee> findList(Map<String,Object> queryMap);
    public List<Interviewee> findListUn(Map<String,Object> queryMap);
    public int countList(Map<String,Object> queryMap);
    public Interviewee findById(String id);
    public Interviewee findRecordById(String id);
    public int updateUnallocatedStatus(Interviewee interviewee);
    public int updateAllocatedStatus(Interviewee interviewee);
    public List<Interviewee> findAll(Map<String,Object> queryMap);
    public int countAll();
}
