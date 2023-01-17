package fit.fitspring.service;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final AccountRepository accountRepository;
    public void registerAccount(AccountForRegisterDto accountDto) {
        Account account = accountDto.toEntity();
        try {
            accountRepository.save(account);
        } catch (DataIntegrityViolationException e){
            throw new DuplicatedAccountException();
        }
    }

    public String getCertificationNumber(String email) {
        String number = "";
        for(int i = 0; i < 6; i++){
            number += Integer.toString((int)(Math.random() * 9));
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("fitiofficial@naver.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("[Fit-I] 인증번호");
        simpleMailMessage.setText(number);
        try {
            javaMailSender.send(simpleMailMessage);
            return number;
        } catch (DataIntegrityViolationException e){
            throw new DuplicatedAccountException();
        }
    }
}
