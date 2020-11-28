package face2face.face2face.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class Mail implements Serializable {
    private String from;//发件人
    private String [] to;//收件人列表
    private String subject;//邮件主题
    private String context;//邮件正文
    private String filepath;//文件路径/面试房间路径
}
