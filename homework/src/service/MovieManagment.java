package service;


import data.GenreTypes;
import data.Movie;
import data.User;
import utils.Util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class MovieManagment {
    private final int MOVIE_SIZE = 5;
    private final int MOVIE_INCREASE_COUNT = 3;
    private Movie[] movies = new Movie[MOVIE_SIZE];


    public MovieManagment() {
        initializeMovies();
    }

    public Movie[] getMovies() {
        return movies;
    }

    private void initializeMovies() {
        movies[0] = new Movie("Yıldızlararası",
                LocalDate.of(2014, 6, 12),
                "Christopher Nolan",
                new String[]{"Matthew McConaughey", "Matt Damon", "Anne Hathaway"},
                new GenreTypes[]{GenreTypes.Macera, GenreTypes.BilimKurgu});
        movies[1] = new Movie("Dövüş Kulubü",
                LocalDate.of(1999, 12, 10),
                "David Fincher",
                new String[]{"Edward Norton", "Helena Bonham Carter", "Brad Pitt"},
                new GenreTypes[]{GenreTypes.Dram,GenreTypes.Gerilim});
        movies[2] = new Movie("Başlangıç",
                LocalDate.of(2010, 7, 30),
                "Christopher Nolan",
                new String[]{"Edward Norton", "Helena Bonham Carter", "Brad Pitt"},
                new GenreTypes[]{GenreTypes.Aksiyon,GenreTypes.BilimKurgu});
        movies[3] = new Movie("Kayıp Kız",
                LocalDate.of(2014, 10, 10),
                "David Fincher",
                new String[]{"Ben Affleck", "rosamund Pike", "Carrie Coon"},
                new GenreTypes[]{GenreTypes.Gerilim,GenreTypes.Gizem});
        movies[4] = new Movie("Karayip Korsanları: Siyah İnci'nin Laneti",
                LocalDate.of(2003, 12, 29),
                "Gore Verbinski",
                new String[]{"Johnny Deep", "Keira Knightley", "Orlando Bloom"},
                new GenreTypes[]{GenreTypes.Macera,GenreTypes.Aksiyon});
    }

    public void reSizeArray() {
        Movie[] newArr = new Movie[movies.length + MOVIE_INCREASE_COUNT];
        System.arraycopy(movies, 0, newArr, 0, movies.length);
        this.movies = newArr;
    }

    public boolean addMovie(Movie movie) {
        boolean added = false;
        if (movie != null) {
            if (Util.allSlotsTaken(movies)) {
                reSizeArray();
            }
            if (!Util.contains(movie, movies)) {
                int index = Util.getAvailableIndex(movies);
                if (index != -1) {
                    movies[index] = movie;
                    added = true;
                }
            }
        }
        return added;
    }

    public Movie getMovieById(String movieId) {
        Movie movie = null;
        for (Movie m : movies) {
            if (m != null && Objects.equals(m.getId(), movieId)) {
                movie = m;
            }
        }
        return movie;
    }

    public Movie getMovieByTitle(String title) {
        Movie movie = null;
        for (Movie m : movies) {
            if (m != null && m.getTitle().equalsIgnoreCase(title)) {
                movie = m;
            }
        }
        return movie;
    }

    public boolean deleteMovie(Movie movie) {
        boolean deleted = false;
        if (movie != null && Util.contains(movie, movies)) {
            int index = Util.getIndex(movie, movies);
            for (int i = index; i < movies.length; i++) {
                if (i == movies.length - 1) {
                    movies[i] = null;
                } else {
                    movies[i] = movies[i + 1];
                }
            }
            deleted = true;
        }
        return deleted;
    }

    public StringBuilder listMovies() {
        StringBuilder list = new StringBuilder();
        if (Util.getAvailableIndex(movies) != 0) {
            list.append(" \u001B[31m **** TÜM FİLMLER **** \u001B[0m \n");
            for (Movie movie : movies) {
                if (movie != null)
                    list
                        .append(" \033[1mID: ").append(movie.getId())
                        .append(" \n\t\033[1mTITLE:\033[0m \u001B[43m").append(movie.getTitle()).append("\u001B[0m")
                        .append(" \n\t\033[1mAVAILABLE: \033[0m").append(movie.isAvailable() ? "\u001B[32m\033[1m" + movie.isAvailable() + " \033[0m\u001B[0m" : "\u001B[31m" + movie.isAvailable() + " \u001B[0m")
                        .append(!movie.isAvailable() ? " \n\t\033[1mWHO TOOK: \033[0m" + movie.getWhoTook().getUserName() : "")
                        .append(" \n\t\033[1mDIRECTORY: \033[0m").append(movie.getDirector())
                        .append(" \n\t\033[1mCAST: \033[0m").append(Arrays.toString(movie.getCast()))
                        .append(" \n\t\033[1mRELEASE DATE: \033[0m").append(movie.getReleaseDate())
                        .append(" \n\t\033[1mTYPES: \033[0m").append(Arrays.toString(movie.getGenres()))
                        .append(" \n\t\033[1mPRESENTATION: \033[0m").append(movie.getPresentation()).append("\n")
                        .append("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n");
            }
        }else{
            list.append("Malesef hiç filim yok.");
        }
        return list;
    }

    public boolean borrowTheMovie(Movie movie, User user, UserManagment userManagment) {
        boolean canBorrow = false;
        int index = Util.getIndex(movie, movies);
        if (movies[index].isAvailable()) {
            movies[index].setWhoTook(user);
            movies[index].setAvailable(false);
            userManagment.addMovieAsBorrowed(movie, user);
            canBorrow = true;
        }
        return canBorrow;
    }

    public void dropTheMovie(Movie movie) {
        int index = Util.getIndex(movie, movies);
        movies[index].setAvailable(true);
    }

    public String[][] suggestMovieList(User currentUser){
        String[][] suggestedMovies = new String[movies.length][2];//[movie][percentage]
        int index = 0;
        for (Movie movie : movies){
            if (!Util.contains(movie,currentUser.getBorrowedMovies())){

                int genresCount = movie.getGenres().length;
                float percentage = (float) 100 / genresCount;
                float matchUpPercentage = 0;
                for (GenreTypes movieGenre : movie.getGenres()){// drama, comedy
                    for (GenreTypes borrowedGenre : currentUser.getBorrowedMoviesGenres()){
                        if (movieGenre == borrowedGenre){
                            matchUpPercentage += percentage;
                        }
                    }
                }
                suggestedMovies[index][0] = movie.getId();
                suggestedMovies[index][1] = "%" + matchUpPercentage;
                index += 1;
            }
        }
        return suggestedMovies;
    }
}
