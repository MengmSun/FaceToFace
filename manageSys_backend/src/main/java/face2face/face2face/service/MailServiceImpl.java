package face2face.face2face.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from ;
    @Override
    public void sendMail(String [] to,String subject,String context,String filepath){
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context,true);
            if(!StringUtils.isEmpty(filepath)){
                //只有文件路径不为空，视为附件
                FileSystemResource resource = new FileSystemResource(new File(filepath));
                helper.addAttachment(resource.getFilename(),resource);
            }
            mailSender.send(message);
            logger.info("邮件发送成功！");
        }catch(Exception e){
            logger.error("邮件发送错误："+e);
        }

    }
}
