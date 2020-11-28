package face2face.face2face.service;

import face2face.face2face.entity.TimeInterval;
import face2face.face2face.mapper.TimeIntervalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TimeIntervalService implements TimeIntervalMapper {
    @Autowired
    private TimeIntervalMapper timeIntervalMapper;
    //返回符合查询条件的所有时间段信息
    @Override
    public List<TimeInterval> findList(Map<String,Object> queryMap){
        return timeIntervalMapper.findList(queryMap);
    }
    //在添加新的面试记录之后删除对应的时间记录
    @Override
    public int delete(TimeInterval timeInterval){
        return timeIntervalMapper.delete(timeInterval);
    }
    @Override
    public int countList(Map<String,Object> queryMap){
        return timeIntervalMapper.countList(queryMap);
    }
    @Override
    public int add(TimeInterval timeInterval) { return timeIntervalMapper.add(timeInterval); }

}
