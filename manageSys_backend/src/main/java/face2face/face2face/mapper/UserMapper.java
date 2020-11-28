package face2face.face2face.mapper;

import face2face.face2face.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    User findById(String id);
    int addUser(User user);
}