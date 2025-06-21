package roomescape.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.member.business.MemberService;
import roomescape.member.business.dto.request.MemberCreateRequest;
import roomescape.member.model.Member;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;
import roomescape.member.presentation.dto.response.MemberGetWebResponse;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberGetWebResponse> createMember(@RequestBody MemberCreateWebRequest requestBody) {
        Member member = memberService.createMember(new MemberCreateRequest(requestBody.email(), requestBody.password(), requestBody.name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberGetWebResponse(member.getEmail(), member.getName()));
    }
}
