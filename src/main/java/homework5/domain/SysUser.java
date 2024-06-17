package homework5.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@NamedEntityGraph(
        name = "SysUser.roles",
        attributeNodes = @NamedAttributeNode("sysRoles")
)
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", length = 36, nullable = false)
    private String userName;

    @Column(name = "encrypted_password", length = 128, nullable = false)
    private String encryptedPassword;

    @Column(name = "enabled", length = 1, nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<SysRole> sysRoles = new HashSet<>();

    public SysUser(Long id, String userName, String encryptedPassword, boolean enabled) {
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.enabled = enabled;
    }

    @Override public String toString() {
        return "SysUser{" +
                "userId=" + getId() +
                ", userName='" + userName + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", enabled=" + enabled +
                ", sysRoles=" + (sysRoles == null ? "" : sysRoles.size()) +
                '}';
    }
}

