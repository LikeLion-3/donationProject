package com.myapp.warmwave.config.oauth.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.config.oauth.CustomUserDetails;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.ALREADY_EXIST_USER;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_USER;


/*
*   Spring Security 에서 제공하는 UserDetailsService 를 구현한 구현체
*   해당 구현체에서 UserDetails 를 User 엔티티로 변환해서 리턴한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER.name()));
        return CustomUserDetails.of(user);
    }

    @Transactional
    public void createUser(UserDetails user) {
        // 사용자가 (이미) 있으면 생성할수 없다.
        if (this.userExists(user.getUsername()))
            throw new CustomException(ALREADY_EXIST_USER);

        userRepository.save(((CustomUserDetails) user).newEntity());
    }

    public boolean userExists(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
