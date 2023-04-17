package com.example.bbs.user;

import com.example.bbs.user.model.User;
import com.example.bbs.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exits"));
    }

    public User add(User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        //user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void updatePassword(User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        userRepository.save(user);
    }

    public User loadById(Long id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public boolean checkByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
