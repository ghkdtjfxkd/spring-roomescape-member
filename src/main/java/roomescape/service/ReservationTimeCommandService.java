package roomescape.service;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.BadRequestException;
import roomescape.exception.custom.ConflictException;
import roomescape.exception.custom.NotFoundException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    @Transactional
    public ReservationTimeResponse create(LocalTime startAt) {
        validateNoDuplicateReservationTime(startAt);
        ReservationTime savedReservationTime = reservationTimeDao.save(ReservationTime.pending(startAt));
        return ReservationTimeResponse.from(savedReservationTime);
    }

    @Transactional
    public void delete(long reservationTimeId) {
        ReservationTime time = reservationTimeDao.findByTimeId(reservationTimeId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TIME_NOT_FOUND));

        if (time.isDeleted()) {
            throw new BadRequestException(ErrorMessage.TIME_ALREADY_DELETED);
        }

        if (reservationDao.existsByTimeId(reservationTimeId)) {
            throw new ConflictException(ErrorMessage.TIME_IN_USE);
        }

        reservationTimeDao.deleteByTimeId(reservationTimeId);
    }

    private void validateNoDuplicateReservationTime(LocalTime startAt) {
        if (reservationTimeDao.existsByStartAt(startAt)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_TIME);
        }
    }
}
