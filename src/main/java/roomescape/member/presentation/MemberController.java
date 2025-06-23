package roomescape.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.member.business.MemberService;
import roomescape.member.model.Member;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;
import roomescape.member.presentation.dto.response.MemberGetWebResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberGetWebResponse> createNormalMember(@RequestBody MemberCreateWebRequest requestBody) {
        Member member = memberService.createNormalMember(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberGetWebResponse(member.getId(), member.getEmail(), member.getName()));
    }

    @AdminLogin
    @GetMapping
    public List<MemberGetWebResponse> findAllMembers() {
        return memberService.findAllMembers().stream()
                .map(member -> new MemberGetWebResponse(member.getId(), member.getEmail(), member.getName()))
                .toList();
    }
}
