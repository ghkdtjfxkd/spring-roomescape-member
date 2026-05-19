package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.TimeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 과거이면 true를 반환한다.")
    void isPastWhenReservationIsInThePast() {
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.AVAILABLE);
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime requestDateTime = LocalDateTime.now();

        assertThat(reservationTime.isPast(date, requestDateTime)).isTrue();
    }

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 미래이면 false를 반환한다.")
    void isPastWhenReservationIsInTheFuture() {
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.AVAILABLE);
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime requestDateTime = LocalDateTime.now();

        assertThat(reservationTime.isPast(date, requestDateTime)).isFalse();
    }
}
