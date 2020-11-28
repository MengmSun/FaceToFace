package face2face.face2face.service;

import face2face.face2face.entity.User;
import face2face.face2face.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserMapper {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findById(String id){
        return userMapper.findById(id);
    }
    @Override
    public int addUser(User user){
        return userMapper.addUser(user);
    }
}
