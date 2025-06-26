package roomescape.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.common.config.SwaggerConfig;
import roomescape.member.business.MemberService;
import roomescape.member.model.Member;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;
import roomescape.member.presentation.dto.response.MemberGetWebResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
@Tag(name = "멤버", description = "멤버와 관련된 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입", description = "일반 권한을 가진 멤버를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    @PostMapping
    public ResponseEntity<MemberGetWebResponse> createNormalMember(@RequestBody MemberCreateWebRequest requestBody) {
        Member member = memberService.createNormalMember(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberGetWebResponse(member.getId(), member.getEmail(), member.getName()));
    }

    @Operation(summary = "모든 멤버 조회", description = "가입되어 있는 모든 멤버를 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @AdminLogin
    @GetMapping
    public List<MemberGetWebResponse> findAllMembers() {
        return memberService.findAllMembers().stream()
                .map(member -> new MemberGetWebResponse(member.getId(), member.getEmail(), member.getName()))
                .toList();
    }
}
