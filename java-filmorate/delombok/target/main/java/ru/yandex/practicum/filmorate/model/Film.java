package ru.yandex.practicum.filmorate.model;

import lombok.NonNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
public class Film {
    @Min(1)
    int id;
    @NotBlank
    String name;
    @Size(min = 0, max = 200)
    String description;
    LocalDate releaseDate;
    @Min(1)
    Duration duration;

    @lombok.Generated
    public Film() {
    }

    @lombok.Generated
    public int getId() {
        return this.id;
    }

    @lombok.Generated
    public String getName() {
        return this.name;
    }

    @lombok.Generated
    public String getDescription() {
        return this.description;
    }

    @lombok.Generated
    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    @lombok.Generated
    public Duration getDuration() {
        return this.duration;
    }

    @lombok.Generated
    public void setId(final int id) {
        this.id = id;
    }

    @lombok.Generated
    public void setName(final String name) {
        this.name = name;
    }

    @lombok.Generated
    public void setDescription(final String description) {
        this.description = description;
    }

    @lombok.Generated
    public void setReleaseDate(final LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @lombok.Generated
    public void setDuration(final Duration duration) {
        this.duration = duration;
    }

    @Override
    @lombok.Generated
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Film)) return false;
        final Film other = (Film) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$releaseDate = this.getReleaseDate();
        final Object other$releaseDate = other.getReleaseDate();
        if (this$releaseDate == null ? other$releaseDate != null : !this$releaseDate.equals(other$releaseDate)) return false;
        final Object this$duration = this.getDuration();
        final Object other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !this$duration.equals(other$duration)) return false;
        return true;
    }

    @lombok.Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Film;
    }

    @Override
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $releaseDate = this.getReleaseDate();
        result = result * PRIME + ($releaseDate == null ? 43 : $releaseDate.hashCode());
        final Object $duration = this.getDuration();
        result = result * PRIME + ($duration == null ? 43 : $duration.hashCode());
        return result;
    }

    @Override
    @lombok.Generated
    public String toString() {
        return "Film(super=" + super.toString() + ", id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", releaseDate=" + this.getReleaseDate() + ", duration=" + this.getDuration() + ")";
    }
}
