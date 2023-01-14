package chunhodong.subway.member.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Member 도메인테스트")
public class MemberTest {

    @DisplayName("비밀번호가 일치하지 않는 경우 예외발생")
    @Test
    void throwsExceptionWhenInvalidPassword() {
        Member member = new Member("test@test.com", "password", 25);
        assertThatThrownBy(() -> member.checkPassword("cmwe")).isInstanceOf(RuntimeException.class);
    }
}
