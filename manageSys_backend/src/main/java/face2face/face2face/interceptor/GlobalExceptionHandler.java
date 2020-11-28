package face2face.face2face.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler
    public Object handleException(Exception e){
        String msg = e.getMessage();
        if(msg==null||msg.equals("")){
            msg = "服务器出错！";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg",msg);
        jsonObject.put("status","error");
        return jsonObject;
    }

}
