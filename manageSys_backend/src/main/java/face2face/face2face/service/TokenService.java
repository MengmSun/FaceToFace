package face2face.face2face.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import face2face.face2face.entity.User;
import org.springframework.stereotype.Service;

@Service("TokenService")
public class TokenService {
    public String getToken(User user){
        String token="";
        //将userId和role保存到token里面，以password作为token的密钥
        token = JWT.create()
                .withAudience(user.getId())
                .withClaim("ROLE",user.getRole())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
