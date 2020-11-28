package face2face.face2face.service;

import face2face.face2face.entity.Record;
import face2face.face2face.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecordService implements RecordMapper{
    @Autowired
    private RecordMapper recordMapper;
    @Override
    public List<Record> findList(Map<String,Object> queryMap){
        return recordMapper.findList(queryMap);
    }
    @Override
    public int countList(Map<String,Object> queryMap){
        return recordMapper.countList(queryMap);
    }
    @Override
    public List<Record> findOnGoingList(Map<String,Object> queryMap) { return recordMapper.findOnGoingList(queryMap); }
    @Override
    public int countOnGoingList(Map<String,Object> queryMap) { return recordMapper.countOnGoingList(queryMap); }
    @Override
    public List<Record> findUndecidedList(Map<String,Object> queryMap){
        return recordMapper.findUndecidedList(queryMap);
    }
    @Override
    public int countUndecidedList(){
        return recordMapper.countUndecidedList();
    }
    @Override
    public List<Record> findDecidedList(Map<String,Object> queryMap){
        return recordMapper.findDecidedList(queryMap);
    }
    @Override
    public int countDecidedList(){
        return recordMapper.countDecidedList();
    }
    @Override
    public int add(Record record){
        return recordMapper.add(record);
    }
    @Override
    public int updateRemark(Record record){
        return recordMapper.updateRemark(record);
    }
    @Override
    public int updateResult(Record record) { return recordMapper.updateResult(record); }
    @Override
    public int updateLink(Record record) { return recordMapper.updateLink(record);}

}
