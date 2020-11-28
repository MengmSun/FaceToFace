package face2face.face2face.service;

import org.springframework.stereotype.Repository;

@Repository
public interface MailService {
    void sendMail(String [] to,String subject,String context,String filepath);

}
