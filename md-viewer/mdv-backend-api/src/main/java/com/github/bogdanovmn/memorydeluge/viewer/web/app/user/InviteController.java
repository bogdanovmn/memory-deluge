package com.github.bogdanovmn.memorydeluge.viewer.web.app.user;

import com.github.bogdanovmn.memorydeluge.viewer.web.app.AbstractController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/invites")
@RequiredArgsConstructor
class InviteController extends AbstractController {
    private final InviteService inviteService;

    @GetMapping
    ModelAndView allActive() {
        return new ModelAndView(
            "invites_active_list",
            "invites",
            inviteService.userInvites(getUser())
        );
    }

    @PostMapping
    String create() {
        inviteService.create(getUser());
        return "redirect:/invites";
    }
}
