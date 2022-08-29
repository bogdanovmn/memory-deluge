package com.github.bogdanovmn.memorydeluge.viewer.web.app;

import com.github.bogdanovmn.memorydeluge.viewer.web.app.config.security.ProjectSecurityService;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.User;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractController {
    @Autowired
    private ProjectSecurityService securityService;

    public User getUser() {
        return securityService.getLoggedInUser();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        User user = getUser();
        return user != null && user.hasRole(UserRole.Type.Admin);
    }
}
