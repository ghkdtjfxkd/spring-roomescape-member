package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeStatus;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.NotFoundException;

@Repository
@RequiredArgsConstructor
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper = (rs, rowNum) -> Theme.of(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("thumbnail_url"),
            rs.getString("description"),
            ThemeStatus.valueOf(rs.getString("status"))
    );

    public Optional<Theme> findByThemeId(long themeId) {
        String sql = "SELECT id, name, thumbnail_url, description, status FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, themeId)
                .stream().findFirst();
    }

    public Optional<Theme> findAvailableByThemeId(long themeId) {
        String sql = "SELECT id, name, thumbnail_url, description, status FROM theme WHERE id = ? AND status = ?";
        return jdbcTemplate.query(sql, rowMapper, themeId, ThemeStatus.AVAILABLE.name())
                .stream().findFirst();
    }

    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("thumbnail_url", theme.thumbnailUrl())
                .addValue("description", theme.description())
                .addValue("status", ThemeStatus.AVAILABLE.name());

        SimpleJdbcInsert themeInsertExecutor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        Number themeId = themeInsertExecutor.executeAndReturnKey(params);

        return Theme.of(
                themeId.longValue(),
                theme.name(),
                theme.thumbnailUrl(),
                theme.description(),
                ThemeStatus.AVAILABLE
        );
    }

    public void delete(long themeId) {
        String sql = "UPDATE theme SET status = ? WHERE id = ?";
        int affected = jdbcTemplate.update(sql, ThemeStatus.DELETED.name(), themeId);

        if(affected == 0) {
            throw new NotFoundException(ErrorMessage.THEME_NOT_FOUND.format(themeId));
        }
    }

    public List<Theme> findAllThemes() {
        String sql = """
                SELECT * FROM theme WHERE status = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, ThemeStatus.AVAILABLE.name());
    }

    public boolean existsByName(String name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM theme
                    WHERE name = ? AND status = ?
                )
                """;
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, name, ThemeStatus.AVAILABLE.name())
        );
    }

    public List<Theme> findSortedPopularThemesBy(LocalDate startAt, LocalDate endAt, int limit) {
        String sql = """
                SELECT 
                    theme.id, 
                    theme.name, 
                    theme.thumbnail_url, 
                    theme.description,
                    theme.status,
                    COUNT(reservation.id) as count
                FROM reservation
                INNER JOIN theme ON reservation.theme_id = theme.id
                WHERE (reservation.date BETWEEN ? AND ?)
                    AND theme.status = ?
                GROUP BY 
                    theme.id,
                    theme.name,
                    theme.thumbnail_url,
                    theme.description,
                    theme.status
                ORDER BY COUNT(reservation.id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, rowMapper, startAt, endAt, ThemeStatus.AVAILABLE.name(), limit);
    }
}
