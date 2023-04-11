package data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class Movie extends BaseData {
    private String title;
    private String presentation;
    private LocalDate releaseDate;
    private final String director;
    private final String[] cast;
    private final GenreTypes[] genres;
    private boolean available;
    private User whoTook;

    public Movie(String title, LocalDate releaseDate, String director, String[] cast, GenreTypes[] genres) {
        super();
        this.title = title;
        this.releaseDate = releaseDate;
        this.director = director;
        this.cast = cast;
        this.genres = genres;
        available = true;
    }

    //region GETTER SETTER
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public String[] getCast() {
        return cast;
    }

    public GenreTypes[] getGenres() {
        return genres;
    }

    public User getWhoTook() {
        return whoTook;
    }

    public void setWhoTook(User whoTook) {
        this.whoTook = whoTook;
    }

    //endregion


    @Override
    public String toString() {
        return "Movie{" +
                "id='" + getId() + '\'' +
                ", title='" + title + '\'' +
                ", presentation='" + presentation + '\'' +
                ", releaseDate=" + releaseDate +
                ", director='" + director + '\'' +
                ", cast=" + Arrays.toString(cast) +
                ", genres=" + Arrays.toString(genres) +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title) && Objects.equals(director, movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director);
    }
}
