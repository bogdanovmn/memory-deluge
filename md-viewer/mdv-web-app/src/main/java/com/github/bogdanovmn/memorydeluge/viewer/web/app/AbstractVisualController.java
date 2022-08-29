package com.github.bogdanovmn.memorydeluge.viewer.web.app;

import com.github.bogdanovmn.memorydeluge.viewer.web.app.config.mustache.Layout;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

public abstract class AbstractVisualController extends AbstractController {
    @Autowired
    private Mustache.Compiler compiler;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @ModelAttribute("layout")
    public Mustache.Lambda layout(Map<String, Object> model) {
        return new Layout(compiler, "main", contextPath);
    }

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("menu", new HeadMenu(currentMenuItem(), getUser()).getItems());
        model.addAttribute("adminMenu", new AdminMenu(currentAdminMenuItem()).getItems());
        model.addAttribute("userName", getUser().getName());
    }

    protected abstract HeadMenu.ITEM currentMenuItem();

    protected AdminMenu.ITEM currentAdminMenuItem() {
        return AdminMenu.ITEM.NONE;
    }
}
