package homework5.security;

import homework5.dao.UserRepository;
import homework5.domain.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<SysUser> sysUser = userRepository.findUsersByUserName(username);
        if (sysUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        List<SimpleGrantedAuthority> authorities = sysUser.get().getSysRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .toList();
        return new User(sysUser.get().getUserName(), sysUser.get().getEncryptedPassword(), authorities);
    }

    public boolean createUser(String login,String password){
        Optional<SysUser> sysUser = userRepository.findUsersByUserName(login);
        if (sysUser.isPresent()){
            throw new RuntimeException("User is already exist.");
        }
        userRepository.save(new SysUser(null, login, passwordEncoder.encode(password), true, null));
        return true;
    }

    public List<SysUser> findAll() {
        return userRepository.findAll();
    }

    public void processOAuthPostLogin(String email) {
        //
    }
}
