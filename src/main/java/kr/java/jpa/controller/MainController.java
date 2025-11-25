package kr.java.jpa.controller;

import kr.java.jpa.model.dto.MemberDTO;
import kr.java.jpa.model.entity.Member;
import kr.java.jpa.model.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Component Scan
@RequestMapping
public class MainController {
    private final MemberRepository memberRepository;

    public MainController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "index";
    }

    @PostMapping
    public String join(
            @ModelAttribute MemberDTO dto
    ) {
        memberRepository.save(
                new Member(dto.username(),
                           dto.password()));
        return "redirect:/";
    }
}
