package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.exception.DomainException;

class ReservationDomainTest {

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 과거이면 예외가 발생한다.")
    void throwExceptionWhenReservationIsInThePast() {
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.AVAILABLE);
        Theme theme = Theme.of(1L, "테마", "http://thumbnail", "설명", ThemeStatus.AVAILABLE);
        LocalDateTime requestedAt = LocalDateTime.now();

        assertThatThrownBy(() -> Reservation.pending("user_a", LocalDate.now().minusDays(1), time, theme, requestedAt))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 미래이면 예외가 발생하지 않는다.")
    void noExceptionWhenReservationIsInTheFuture() {
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.AVAILABLE);
        Theme theme = Theme.of(1L, "테마", "http://thumbnail", "설명", ThemeStatus.AVAILABLE);
        LocalDateTime requestedAt = LocalDateTime.now();

        assertDoesNotThrow(() -> Reservation.pending("user_a", LocalDate.now().plusDays(1), time, theme, requestedAt));
    }
}
