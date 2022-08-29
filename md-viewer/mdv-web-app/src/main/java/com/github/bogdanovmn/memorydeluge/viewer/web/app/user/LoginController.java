package com.github.bogdanovmn.memorydeluge.viewer.web.app.user;

import com.github.bogdanovmn.memorydeluge.viewer.web.app.AbstractMinVisualController;
import com.github.bogdanovmn.memorydeluge.viewer.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
class LoginController extends AbstractMinVisualController {
    @GetMapping("/login")
    ModelAndView form(Model model, String error) {
        if (getUser() != null) {
            return new ModelAndView("redirect:" + HeadMenu.DEFAULT_PAGE);
        }

        if (error != null) {
            model.addAttribute("customError", "Попробуйте еще разок");
        }

        return new ModelAndView("login", model.asMap());
    }
}
