package homework5.security;

import homework5.domain.AbstractEntity;
import homework5.domain.SysUser;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditorAwareImpl implements AuditorAware<SysUser> {

    public Optional<SysUser> getCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthentication) {
            JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
            return Optional.ofNullable(authentication.getPrincipal());
        }
        return Optional.empty();
    }

    @PrePersist
    public void setCreatedBy(AbstractEntity entity) {
        Optional<SysUser> auditor = getCurrentAuditor();
        if (auditor.isPresent()) {
            entity.setCreatedBy(auditor.get());
        }
    }

    @PreUpdate
    public void setLastModifiedBy(AbstractEntity entity) {
        Optional<SysUser> auditor = getCurrentAuditor();
        if (auditor.isPresent()) {
            entity.setLastModifiedBy(auditor.get());
        }
    }
}
