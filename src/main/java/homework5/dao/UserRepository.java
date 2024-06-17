package homework5.dao;

import homework5.domain.SysUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SysUser, Long> {

    @EntityGraph(value = "SysUser.roles")
    Optional<SysUser> findUsersByUserName(String userName);

    @EntityGraph(value = "SysUser.roles")
    List<SysUser> findAll();

    @EntityGraph(value = "SysUser.roles")
    Optional<SysUser> findById(Long id);
}
