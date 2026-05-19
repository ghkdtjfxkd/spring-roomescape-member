package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.exception.DomainException;

class ThemeTest {

    @Test
    @DisplayName("유효한 URL로 테마를 생성하면 정상적으로 생성된다.")
    void createThemeWithValidUrl() {
        Theme theme = Theme.pending("공포의 저택", "http://localhost/thumbnail", "설명");

        assertThat(theme.thumbnailUrl()).isEqualTo("http://localhost/thumbnail");
    }

    @Test
    @DisplayName("유효하지 않은 URL로 테마를 생성하면 DomainException이 발생한다.")
    void createThemeWithInvalidUrl() {
        assertThatThrownBy(() -> Theme.pending("공포의 저택", "잘못된 형식", "설명"))
                .isInstanceOf(DomainException.class)
                .hasMessage("유효한 URL 형식이 아닙니다.");
    }
}
