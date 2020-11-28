package face2face.face2face.controller;

import com.alibaba.fastjson.JSONObject;
import face2face.face2face.entity.User;
import face2face.face2face.service.TokenService;
import face2face.face2face.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginAndRegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    /*@GetMapping
    public ModelAndView getList(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }*/

    @PostMapping("/login")
    public Object login(User user) {
        JSONObject jsonObject = new JSONObject();
        User userForBase = userService.findById(user.getId());
        if (userForBase == null) {
            jsonObject.put("status","error");
            jsonObject.put("msg", "登录失败，用户不存在！");
            return jsonObject;
        } else {
            if (!userForBase.getPassword().equals(user.getPassword())) {
                jsonObject.put("status","error");
                jsonObject.put("msg", "登录失败，密码错误！");
                return jsonObject;
            } else {
                String token = tokenService.getToken(userForBase);
                jsonObject.put("status","success");
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                return jsonObject;
            }
        }
    }


}
