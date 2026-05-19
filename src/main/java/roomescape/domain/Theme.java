package roomescape.domain;

import java.net.URI;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import roomescape.domain.exception.DomainException;

@RequiredArgsConstructor
public class Theme {

    private final Long id;
    private final String name;
    private final String thumbnailUrl;
    private final String description;
    private final ThemeStatus status;

    public static Theme pending(String name, String thumbnailUrl, String description) {
        validateUrl(thumbnailUrl);
        return new Theme(null, name, thumbnailUrl, description, ThemeStatus.DRAFT);
    }

    private static void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new DomainException("유효한 URL 형식이 아닙니다.");
        }
    }

    public static Theme of(long id, String name, String thumbnailUrl, String description) {
        return new Theme(id, name, thumbnailUrl, description, ThemeStatus.AVAILABLE);
    }

    public Theme deleted() {
        return new Theme(this.id, this.name, this.thumbnailUrl ,this.description, ThemeStatus.DELETED);
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    public String description() {
        return description;
    }

    public ThemeStatus status() {
        return status;
    }
}
