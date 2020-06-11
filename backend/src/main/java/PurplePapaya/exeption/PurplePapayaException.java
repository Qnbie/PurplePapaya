package PurplePapaya.exeption;

import org.springframework.mail.MailException;

public class PurplePapayaException extends Throwable{
    public PurplePapayaException(String exMessage, MailException e) {
        super(exMessage);
    }
}
