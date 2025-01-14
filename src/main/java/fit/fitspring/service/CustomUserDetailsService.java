package fit.fitspring.service;

import fit.fitspring.controller.dto.account.AccountSessionDto;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.utils.AccountAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 해당 유저를 찾을 수 없습니다."));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return createUserDetails(account);

    }

    // 해당 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Account account) {
        //이곳에 활성화 상태 등의 validation 추가하면 됨
//        if (!account.isActivated()) {
//            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//        }

        /*return User.builder()
                .username(account.getEmail())
                .password(passwordEncoder.encode(account.getPassword()))
                .roles(account.getAccountType().toString())
                .build();*/
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + account.getAccountType().toString()));
        return new AccountAdapter(account, roles);
    }
}
