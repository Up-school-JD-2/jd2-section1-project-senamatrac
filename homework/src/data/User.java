package data;

import java.util.Objects;

public class User extends BaseData {
    private final int USER_BORROW_MOVIE_LIMIT = 5;
    private final String userName;
    private String email;
    private String password;
    private Movie[] borrowedMovies = new Movie[USER_BORROW_MOVIE_LIMIT];
    private GenreTypes[] borrowedMoviesGenres = new GenreTypes[5];

    public User(String name, String email, String password) {
        super();
        this.userName = name;
        this.email = email;
        this.password = password;
    }

    //region GETTER SETTER
    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Movie[] getBorrowedMovies() {
        return borrowedMovies;
    }

    public void setBorrowedMovies(Movie[] borrowedMovies) {
        this.borrowedMovies = borrowedMovies;
    }

    public GenreTypes[] getBorrowedMoviesGenres() {
        return borrowedMoviesGenres;
    }

    public void setBorrowedMoviesGenres(GenreTypes[] borrowedMoviesGenres) {
        this.borrowedMoviesGenres = borrowedMoviesGenres;
    }

    //endregion

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", name='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email);
    }
}
