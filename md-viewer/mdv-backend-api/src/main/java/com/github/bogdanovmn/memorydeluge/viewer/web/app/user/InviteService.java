package com.github.bogdanovmn.memorydeluge.viewer.web.app.user;

import com.github.bogdanovmn.memorydeluge.viewer.model.entity.Invite;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.InviteRepository;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class InviteService {
    @Value("${invite.max-active}")
    private int maxActive;
    private final InviteRepository inviteRepository;

    InviteService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    UserInvites userInvites(User user) {
        return UserInvites.from(
            inviteRepository.getAllByCreator(user)
        );
    }

    void create(User user) {
        if (isCreateInviteLimitReached(user)) {
            log.info("Wait some time for new invite");
        } else {
            inviteRepository.save(
                new Invite().setCreator(user)
            );
        }
    }

    Invite findByCode(String code) {
        return inviteRepository.findFirstByCode(code);
    }

    private boolean isCreateInviteLimitReached(User user) {
        UserInvites invites = UserInvites.from(
            inviteRepository.getAllByCreator(user)
        );
        return invites.activeCount() > maxActive;
    }
}
