package roomescape.member.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.member.business.dto.request.MemberCreateRequest;
import roomescape.member.exception.DuplicatedMemberException;
import roomescape.member.model.Member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void 멤버를_생성할_수_있다() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME);

        // When
        Member member = memberService.createNormalMember(memberCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isNotNull();
            softAssertions.assertThat(member.getEmail()).isEqualTo(TestConstant.MEMBER_EMAIL);
            softAssertions.assertThat(member.getPassword()).isEqualTo(TestConstant.MEMBER_PASSWORD);
            softAssertions.assertThat(member.getName()).isEqualTo(TestConstant.MEMBER_NAME);
        });
    }

    @Test
    void 중복된_이메일으로는_멤버를_생성할_수_없다() {
        // Given
        memberService.createNormalMember(new MemberCreateRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME));

        // When & Then
        assertThatThrownBy(() -> memberService.createNormalMember(new MemberCreateRequest(TestConstant.MEMBER_EMAIL, "다른 비밀번호", "다른 이름")))
                .isInstanceOf(DuplicatedMemberException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }
}