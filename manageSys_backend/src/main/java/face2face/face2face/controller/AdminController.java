package face2face.face2face.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import face2face.face2face.annotation.Admin;
import face2face.face2face.annotation.UserLoginToken;
import face2face.face2face.entity.Interviewee;
import face2face.face2face.entity.Interviewer;
import face2face.face2face.entity.Record;
import face2face.face2face.entity.TimeInterval;
import face2face.face2face.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    RecordService recordService;
    @Autowired
    IntervieweeService intervieweeService;
    @Autowired
    InterviewerService interviewerService;
    @Autowired
    TimeIntervalService timeIntervalService;
    @Autowired
    MailService mailService;
    /*
    @Autowired
    FinishedResultService finishedResultService;*/


    //获取所有候选人的信息
    @UserLoginToken
    @Admin
    @GetMapping("/applicant")
    public Object getAllApplicant(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        //queryMap.put("status","");
        jsonObject.put("data",intervieweeService.findAll(queryMap));
        jsonObject.put("total",intervieweeService.countAll());
        jsonObject.put("status","success");
        return jsonObject;
    }

    //获取所有待分配的候选人的信息
    @UserLoginToken
    @Admin
    @GetMapping("/applicant/unallocated")
    public Object getUnallocatedApplicant(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        queryMap.put("status","unallocated");
        jsonObject.put("data",intervieweeService.findListUn(queryMap));
        jsonObject.put("total",intervieweeService.countList(queryMap));
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获取所有已分配的候选人的信息
    @UserLoginToken
    @Admin
    @GetMapping("/applicant/allocated")
    public Object getAllocatedApplicants(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        queryMap.put("status","allocated");
        jsonObject.put("data",intervieweeService.findList(queryMap));
        jsonObject.put("total",intervieweeService.countList(queryMap));
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获取所有(可选)面试官的信息
    //传入参数时间段，在时间段之内有空的所有面试官信息会被显示
    @UserLoginToken
    @Admin
    @GetMapping("/interviewer/free")
    public Object getFreeInterviewers(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit,
            @RequestParam(value = "startTime", required = true,defaultValue = "0000-01-01 00:00:00") String startTime,
            @RequestParam(value = "endTime" ,required = true,defaultValue = "9999-01-01 23:59:59") String endTime
            ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        queryMap.put("startTime",startTime);
        queryMap.put("endTime",endTime);
        jsonObject.put("data",timeIntervalService.findList(queryMap));
        jsonObject.put("total",timeIntervalService.countList(queryMap));
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获取所有面试官的信息
    @UserLoginToken
    @Admin
    @GetMapping("/interviewer/info")
    public Object getAllInterviewers(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        jsonObject.put("data",interviewerService.findAll(queryMap));
        jsonObject.put("total",interviewerService.countAll());
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获取某个面试官的所有信息
    //传入参数为面试官的id
    //返回参数为面试官的个人信息和空闲时间列表
    @UserLoginToken
    @Admin
    @GetMapping("/interviewer/time")
    public Object getInterviewer(
            @RequestParam(value = "id") String id
    ){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",interviewerService.findById(id));
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获得所有的面试记录
    @UserLoginToken
    @Admin
    @GetMapping("/record/list")
    public Object getRecordList(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        jsonObject.put("data",recordService.findList(queryMap));
        jsonObject.put("total",recordService.countList(queryMap));
        jsonObject.put("status","success");
        return jsonObject;
    }
    //创建新的面试，并且给面试官和面试者发送邮件
    @UserLoginToken
    @Admin
    @PostMapping("/record/add")
    public Object addNewRecord(String interviewerId, String intervieweeId, String startTime,String endTime){
        Interviewee interviewee = intervieweeService.findById(intervieweeId);
        Interviewer interviewer = interviewerService.findById2(interviewerId);
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setInterviewer(interviewer);
        timeInterval.setStartTime(startTime);
        timeInterval.setEndTime(endTime);
        Record record = new Record();
        record.setInterviewee(interviewee);
        record.setInterviewer(interviewer);
        record.setTimeInterval(timeInterval);
        //生成随机房间号
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random1=new Random();
        //指定字符串长度，拼接字符并toString
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < 10; i++) {
            //获取指定长度的字符串中任意一个字符的索引值
            int number=random1.nextInt(str.length());
            //根据索引值获取对应的字符
            char charAt = str.charAt(number);
            sb.append(charAt);
        }
        String room = new String(sb);
        int index = interviewee.getIndex();
        index = index+1;
        String interviewee_link = new String("https://sinon02.xyz:8080/#/"+"?interviewee_id="+intervieweeId+"&interviewer_id=Interviewer"+"&index="+index+"&room="+room);
        String interviewer_link = new String("https://sinon02.xyz:8080/#/"+"?interviewer_id="+interviewerId+"&interviewee_id="+intervieweeId+"&index="+index+"&room="+room);
        record.setLink(interviewee_link);
        record.setIndex(index);
        JSONObject jsonObject = new JSONObject();

        //将面试者的状态变为已分配
        try{
            if(intervieweeService.updateUnallocatedStatus(interviewee)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","变更面试者状态失败！");
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(recordService.add(record)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","创建新的面试失败！");
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(timeIntervalService.add(timeInterval)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","添加新的时间段失败！");
                return  jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //给面试官和面试者发送邮件
        String[] to_interviewee = {interviewee.getEmail()};
        String[] to_interviewer = {interviewer.getEmail()};
        mailService.sendMail(to_interviewee,"面试通知","您好，"+interviewee.getName()+"恭喜您通过了上一轮筛选，现邀请您参与我司面试环节。"+"面试的时间安排为："+timeInterval.getStartTime().toString()+"面试房间为："+interviewee_link+"  如有任何相关问题可随时与我联系，感谢你的参与！","");
        mailService.sendMail(to_interviewer,"面试通知","您好，"+interviewer.getName()+"面试官，现邀请您参与我司面试环节。"+"面试的时间安排为："+timeInterval.getStartTime().toString()+"面试房间为："+interviewer_link+"  如有任何相关问题可随时与我联系，感谢你的参与！","");
        jsonObject.put("data",record);
        jsonObject.put("msg","创建新的面试成功且邮件发送成功！");
        jsonObject.put("status","success");
        return jsonObject;
    }

    //获取所有正在面试中面试信息
    @UserLoginToken
    @Admin
    @GetMapping("/record/ongoing")
    public Object getOngoing(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        queryMap.put("dNow", new String(ft.format(dNow)));
        jsonObject.put("data",recordService.findOnGoingList(queryMap));
        jsonObject.put("total",recordService.countOnGoingList(queryMap));
        jsonObject.put("status","success");
        //System.out.println(ft.format(dNow));
        return jsonObject;
    }
    //获取所有未处理面试信息 包括未评价和无结果
    @UserLoginToken
    @Admin
    @GetMapping("/record/undecided")
    public Object getUndecidedList(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap  = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        jsonObject.put("data",recordService.findUndecidedList(queryMap));
        jsonObject.put("total",recordService.countUndecidedList());
        jsonObject.put("status","success");
        return jsonObject;
    }
    //获取所有已处理面试信息 评价和结果都有的
    @UserLoginToken
    @Admin
    @GetMapping("/record/decided")
    public Object getDecidedList(
            @RequestParam(value = "page", required = true,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = true,defaultValue = "20") int limit
    ){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> queryMap  = new HashMap<String,Object>();
        queryMap.put("offset1",(page-1)*limit);
        queryMap.put("offset2",(page)*limit);
        jsonObject.put("data",recordService.findDecidedList(queryMap));
        jsonObject.put("total",recordService.countDecidedList());
        jsonObject.put("status","success");
        return jsonObject;
    }
    //更新某次面试的面试评价
    @PostMapping("/record/updateRemark")
    public Object updateRecordRemark(String interviewerId,String intervieweeId,String remark,Integer index,String score){
        JSONObject jsonObject = new JSONObject();
        Interviewee interviewee = intervieweeService.findById(intervieweeId);
        Interviewer interviewer = interviewerService.findById2(interviewerId);
        //jsonObject.put("interviewer",interviewer);
        Record record = new Record();
        record.setInterviewee(interviewee);
        record.setInterviewer(interviewer);
        record.setIndex(index);
        record.setRemark(remark);
        record.setScore(score);
        jsonObject.put("record",record);
        System.out.println(record.toString());
        try{
            if(recordService.updateRemark(record)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","变更面试评价失败！");
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        jsonObject.put("status","success");
        jsonObject.put("msg","面试评价更新成功！");
        return jsonObject;
    }

    //更新某次的面试结果，并相应发送邮件
    //在进入下一轮时，面试者的状态重新变为待分配状态
    @UserLoginToken
    @Admin
    @PostMapping("/record/updateResult")
    public Object updateRecordResult(String interviewerId,String intervieweeId,String result,int index){
        JSONObject jsonObject = new JSONObject();
        Interviewee interviewee = intervieweeService.findById(intervieweeId);
        Interviewer interviewer = interviewerService.findById2(interviewerId);
        Record record = new Record();
        record.setInterviewee(interviewee);
        record.setInterviewer(interviewer);
        record.setIndex(index);
        String [] to = {interviewee.getEmail()};
        record.setResult(result);

        if(result.equals("进入下一轮")){
            mailService.sendMail(to,"面试进度","恭喜进入下一轮面试！","");
            try{
                if(intervieweeService.updateAllocatedStatus(interviewee)<=0){
                    jsonObject.put("status","error");
                    jsonObject.put("msg","变更面试者状态失败！");
                    return jsonObject;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(result.equals("录用")){
            //如果有文件需要发送就将文件路径放在filepath
            mailService.sendMail(to,"面试成功","恭喜面试成功！","");
        }
        else if(result.equals("拒绝")){
            mailService.sendMail(to,"面试失败","您的面试流程已经终止！","");
        }
        try{
            if(recordService.updateResult(record)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","变更面试结果失败！");
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        jsonObject.put("status","success");
        jsonObject.put("msg","面试记录更新成功！");
        return jsonObject;
    }
    //更新面试录像
    //@UserLoginToken
    //@Admin
    @PostMapping("/record/updateLink")
    public Object updateRecordLink(String interviewerId,String intervieweeId,String link,int index){
        JSONObject jsonObject = new JSONObject();
        Interviewee interviewee = intervieweeService.findById(intervieweeId);
        Interviewer interviewer = interviewerService.findById2(interviewerId);
        Record record = new Record();
        record.setInterviewee(interviewee);
        record.setInterviewer(interviewer);
        record.setIndex(index);
        record.setLink(link);
        try{
            if(recordService.updateLink(record)<=0){
                jsonObject.put("status","error");
                jsonObject.put("msg","变更面试录像失败！");
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        jsonObject.put("status","success");
        jsonObject.put("msg","面试录像更新成功！");
        return jsonObject;
    }
}
