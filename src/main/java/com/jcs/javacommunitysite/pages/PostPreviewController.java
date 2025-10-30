package com.jcs.javacommunitysite.pages;

import com.jcs.javacommunitysite.forms.MarkdownPreviewForm;
import com.jcs.javacommunitysite.util.MarkdownUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostPreviewController {

    @PostMapping("/previewMarkdown")
    public String previewMarkdown(@ModelAttribute MarkdownPreviewForm form, Model model) {
        model.addAttribute("rawHtml", MarkdownUtil.render(form.getContent()));
        return "raw";
    }
}
