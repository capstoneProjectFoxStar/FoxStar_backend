package org.foxstar.petdiary;

mport lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.foxstar.petdiary.domain.entity.UserRole;
import org.foxstar.petdiary.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import foxstar.petdiary.domain.UserRole;
import foxstar.petdiary.domain.entity.User;
import foxstar.petdiary.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class MakeInitData {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void makeAdminAndUser() {
        User admin1 = User.builder()
                .loginId("admin1")
                .password("1234")
                .nickname("관리자1")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin1);

        User user1 = User.builder()
                .loginId("user1")
                .password("1234")
                .nickname("User1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user1);

        User admin2 = User.builder()
                .loginId("admin2")
                .password(encoder.encode("1234"))
                .nickname("관리자")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin2);

        User user2 = User.builder()
                .loginId("user")
                .password(encoder.encode("1234"))
                .nickname("유저1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user2);
    }
}