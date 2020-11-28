package face2face.face2face.service;

import face2face.face2face.entity.Interviewee;
import face2face.face2face.entity.Interviewer;
import face2face.face2face.mapper.InterviewerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InterviewerService implements InterviewerMapper{
    @Autowired
    private InterviewerMapper interviewerMapper;
    //根据ID返回对应的面试官信息
    @Override
    public Interviewer findById(String id){
        return interviewerMapper.findById(id);
    }
    @Override
    public Interviewer findById2(String id) { return interviewerMapper.findById2(id); }
    @Override
    public List<Interviewer> findAll(Map<String, Object> queryMap){
        return interviewerMapper.findAll(queryMap);
    }
    @Override
    public int countAll(){ return interviewerMapper.countAll(); }
}
