package homework5.service;

import homework5.dao.UserRepository;
import homework5.domain.SysUser;
import org.springframework.transaction.annotation.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Optional<SysUser> getByLogin(@NonNull String login) {
        return userRepository.findUsersByUserName(login);
    }

    public Optional<SysUser> findUsersByUserName(String userName) {
        return userRepository.findUsersByUserName(userName);
    };
}