package roomescape.member.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.member.exception.DuplicatedMemberException;
import roomescape.member.model.Member;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;

import static org.assertj.core.api.Assertions.assertThat;
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
        MemberCreateWebRequest memberCreateWebRequest = new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME);

        // When
        Member member = memberService.createNormalMember(memberCreateWebRequest);

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
        memberService.createNormalMember(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME));

        // When & Then
        assertThatThrownBy(() -> memberService.createNormalMember(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, "다른 비밀번호", "다른 이름")))
                .isInstanceOf(DuplicatedMemberException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

    @Test
    void 저장되어_있는_모든_멤버를_조회할_수_있다() {
        // Given
        Member member = memberService.createNormalMember(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME));

        // When & Then
        assertThat(memberService.findAllMembers()).containsExactlyInAnyOrder(member);
    }
}