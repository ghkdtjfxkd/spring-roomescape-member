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

    @Test
    @DisplayName("DELETED 상태의 테마는 isDeleted()가 true를 반환한다.")
    void isDeletedReturnsTrueWhenDeleted() {
        Theme theme = Theme.of(1L, "공포의 저택", "http://localhost/thumbnail", "설명", ThemeStatus.DELETED);

        assertThat(theme.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("AVAILABLE 상태의 테마는 isDeleted()가 false를 반환한다.")
    void isDeletedReturnsFalseWhenAvailable() {
        Theme theme = Theme.of(1L, "공포의 저택", "http://localhost/thumbnail", "설명", ThemeStatus.AVAILABLE);

        assertThat(theme.isDeleted()).isFalse();
    }
}
