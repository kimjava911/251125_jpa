package kr.java.jpa.controller;

import kr.java.jpa.model.dto.MemberDTO;
import kr.java.jpa.model.entity.Member;
import kr.java.jpa.model.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Component Scan
@RequestMapping
public class MainController {
    private final MemberRepository memberRepository;

    public MainController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") long id, Model model) {
        model.addAttribute("member", memberRepository.findById(id));
        return "detail";
    }

    @PostMapping("/{id}/password")
    public String updatePassword(
            @PathVariable("id") long id,
            @RequestParam("password") String password) {
        memberRepository.updatePassword(id, password);
        return "redirect:/{id}";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable("id") long id,
            @ModelAttribute MemberDTO dto) {
        Member oldMember = memberRepository.findById(id);
        Member newMember = new Member(dto.username(), dto.password());
        newMember.setMemberId(oldMember.getMemberId()); // <- DTO에 포함이 되지 X
        newMember.setCreatedAt(oldMember.getCreatedAt()); // <- Form -> Hidden 등으로 하기도 좀...
        memberRepository.update(newMember);
        return "redirect:/";
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
