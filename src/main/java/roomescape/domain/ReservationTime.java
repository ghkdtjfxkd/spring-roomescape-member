package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final TimeStatus status;

    public static ReservationTime pending(LocalTime startAt) {
        return new ReservationTime(null, startAt, TimeStatus.DRAFT);
    }

    public static ReservationTime of(long id, LocalTime startAt, TimeStatus status) {
        return new ReservationTime(id, startAt, status);
    }

    public long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }

    public boolean isPast(LocalDate date, LocalDateTime requestDateTime) {
        return requestDateTime.isAfter(LocalDateTime.of(date, this.startAt));
    }

    public TimeStatus status() {
        return status;
    }
}
