package face2face.face2face.service;

import face2face.face2face.entity.Interviewee;
import face2face.face2face.entity.Interviewer;
import face2face.face2face.mapper.IntervieweeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IntervieweeService implements IntervieweeMapper {
    @Autowired
    private IntervieweeMapper intervieweeMapper;
    //返回符合查找条件的所有面试者信息
    @Override
    public List<Interviewee> findAll(Map<String,Object> queryMap){
        return intervieweeMapper.findAll(queryMap);
    }
    @Override
    public int countAll(){
        return intervieweeMapper.countAll();
    }
    @Override
    public List<Interviewee> findList(Map<String,Object> queryMap){
        return intervieweeMapper.findList(queryMap);
    }
    @Override
    public List<Interviewee> findListUn(Map<String,Object> queryMap){
        return intervieweeMapper.findListUn(queryMap);
    }
    //返回符合查找条件的面试者信息的数目
    @Override
    public int countList(Map<String,Object> queryMap){
        return intervieweeMapper.countList(queryMap);
    }
    //返回根据ID查找的面试者信息
    @Override
    public Interviewee findById(String id){
        return intervieweeMapper.findById(id);
    }
    //返回根据ID查找的面试者信息，这次返回信息中包含了recordList列表
    @Override
    public Interviewee findRecordById(String id){
        return intervieweeMapper.findRecordById(id);
    }
    //更新面试者的状态，在添加面试记录之后为已分配，进入下一轮时重新变为未分配
    @Override
    public int updateUnallocatedStatus(Interviewee interviewee){
        return intervieweeMapper.updateUnallocatedStatus(interviewee);
    }
    public int updateAllocatedStatus(Interviewee interviewee){
        return intervieweeMapper.updateAllocatedStatus(interviewee);
    }
}
