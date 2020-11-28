package face2face.face2face.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    String id;
    String password;
    String role;//用户在系统中的角色
}
