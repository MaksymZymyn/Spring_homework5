package homework5.security;

import homework5.domain.AbstractEntity;
import homework5.domain.SysUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class CustomAuditingEntityListener {

    private SysUser getCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthentication) {
            JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
            return authentication.getPrincipal();
        }
        return null;
    }

    @PrePersist
    public void setCreatedBy(AbstractEntity entity) {
        SysUser auditor = getCurrentAuditor();
        if (auditor != null) {
            entity.setCreatedBy(auditor);
        }
    }

    @PreUpdate
    public void setLastModifiedBy(AbstractEntity entity) {
        SysUser auditor = getCurrentAuditor();
        if (auditor != null) {
            entity.setLastModifiedBy(auditor);
        }
    }
}
