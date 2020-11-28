package face2face.face2face.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import face2face.face2face.annotation.Admin;
import face2face.face2face.annotation.Interviewee;
import face2face.face2face.annotation.Interviewer;
import face2face.face2face.annotation.UserLoginToken;
import face2face.face2face.entity.User;
import face2face.face2face.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse,
                             Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");//从http请求头中取出token
        //如果不是映射到方法，直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查有没有需要用户权限的注释
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                //执行认证
                if (token == null) {
                    throw new RuntimeException("无token,请重新登录！");
                }
                //获取token中的用户ID
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException e) {
                    throw new RuntimeException("401");
                }
                //获取token中的用户角色
                Claim userRole;
                try{
                    userRole = JWT.decode(token).getClaim("ROLE");
                }catch(JWTDecodeException e){
                    throw new RuntimeException("401");
                }
                //到数据库中去查
                User user = userService.findById(userId);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录！");
                }
                //验证用户角色
                //管理员
                if(method.isAnnotationPresent(Admin.class)){
                    if(!userRole.asString().equals("admin")){
                        throw new RuntimeException("当前用户无权限访问该页面！");
                    }
                }
                //面试者
                if(method.isAnnotationPresent(Interviewee.class)){
                    if(!userRole.asString().equals("interviewee")){
                        throw new RuntimeException("当前用户无权限访问该页面！");
                    }
                }
                //面试官
                if(method.isAnnotationPresent(Interviewer.class)){
                    if(!userRole.asString().equals("interviewer")){
                        throw new RuntimeException("当前用户无权限访问该页面！");
                    }
                }

                //验证token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTDecodeException e) {
                    throw new RuntimeException("401");
                }
                return true;

            }
        }
        return true;

    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object object, ModelAndView modelAndView)throws Exception{

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
                                Object object, Exception e)throws Exception{

    }
}

