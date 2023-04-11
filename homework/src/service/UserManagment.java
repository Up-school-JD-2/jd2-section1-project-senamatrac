package service;

import data.GenreTypes;
import data.Movie;
import data.User;
import utils.Util;

import java.util.Arrays;
import java.util.Objects;

public class UserManagment {
    private final int USER_SIZE = 2;
    private final int USER_INCREASE_COUNT = 3;
    User[] users = new User[USER_SIZE];

    public UserManagment() {
        initializeUsers();
    }

    private void initializeUsers(){
        users[0] = new User("senamatrac","senamatrac@gmail.com","123");
        users[1] = new User("sarematrac","sarematrac@gmail.com","123");
    }
    public StringBuilder listUsers() {
        StringBuilder list = new StringBuilder(" \u001B[31m **** TÜM FİLMLER **** \u001B[0m \n");
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null)
                list.append(i).
                        append(" NAME: ").append(users[i].getUserName()).
                        append(" EMAIL: ").append(users[i].getEmail()).
                        append(" PASSWORD: ").append(users[i].getPassword()).
                        append(" ID: ").append(users[i].getId()).append("\n");
        }
        return list;
    }

    public User enterLibrarySystem(String name, String password){
        User currentUser = null;
        for (User user : users){
            if(user != null && user.getUserName().equals(name.trim()) && user.getPassword().equals(password.trim())){
                currentUser = user;
                break;
            }
        }
        return currentUser;
    }

    public void reSizeUserArray() {
        User[] newArr = new User[users.length + USER_INCREASE_COUNT];
        System.arraycopy(users, 0, newArr, 0, users.length);
        this.users = newArr;
    }

    public boolean addUser(User user) {
        boolean added = false;
        if (user != null) {
            if (Util.allSlotsTaken(users)) {
                reSizeUserArray();
            }
            if (!Util.contains(user,users)) {
                int index = Util.getAvailableIndex(users);
                if (index != -1) {
                    users[index] = user;
                    added = true;
                }
            }
        }
        return added;
    }

    public User getUserById(String userId){
        User user = null;
        for (User u: users){
            if (u != null && Objects.equals(u.getId(), userId)){
                user = u;
            }
        }
        return user;
    }
    public User getUserByName(String name){
        User user = null;
        for (User u: users){
            if (u != null && u.getUserName().equalsIgnoreCase(name)){
                user = u;
            }
        }
        return user;
    }
    public User getUserByEmail(String email){
        User user = null;
        for (User u: users){
            if (u != null && u.getEmail().equalsIgnoreCase(email)){
                user = u;
            }
        }
        return user;
    }

    public boolean deleteUser(User user) {
        boolean deleted = false;
        if (user != null && Util.contains(user,users)) {
            int index = Util.getIndex(user,users);
            for (int i = index; i < users.length; i++) {
                if (i == users.length - 1) {
                    users[i] = null;
                } else {
                    users[i] = users[i + 1];
                }
            }
            deleted = true;
        }
        return deleted;
    }

    public boolean addMovieAsBorrowed(Movie movie,User currentUser){
        boolean added = false;
        if (!Util.allSlotsTaken(currentUser.getBorrowedMovies())){
            int index = Util.getAvailableIndex(currentUser.getBorrowedMovies());
            if (index != -1) {
                currentUser.getBorrowedMovies()[index] = movie;
                addGenre(movie,currentUser);
                added = true;
            }
        }
        return added;
    }

    public StringBuilder borrowedMovieList(User currentUser){
        StringBuilder list = new StringBuilder();
        int totalElement = Util.getAvailableIndex(currentUser.getBorrowedMovies());
        if (totalElement != 0){
            list.append("Ödünç aldığınız film sayısı: ").append(totalElement).append("\n");
            for (Movie movie : currentUser.getBorrowedMovies()){
                if (movie != null){
                    list.append(" \033[1mTITLE: \033[0m")
                            .append(movie.getTitle())
                            .append(" \033[1mTYPES: \033[0m")
                            .append(Arrays.toString(movie.getGenres())).append(" \033[1mCAST: \033[0m")
                            .append(Arrays.toString(movie.getCast())).append("\n");
                }
            }
        }else {
            list.append("Ödünç aldığınız film sayısı: 0");
        }
        return list;
    }

    public void reSizeGenreArray(User currentUser) {
        GenreTypes[] newArr = new GenreTypes[currentUser.getBorrowedMoviesGenres().length + 5];
        System.arraycopy(users, 0, newArr, 0, users.length);
        currentUser.setBorrowedMoviesGenres(newArr);
    }
    public void addGenre(Movie movie,User currentUser){
        for (GenreTypes newGenre : movie.getGenres()){//Drama,Romantic,Science
            boolean isExist = false;
            for (GenreTypes genreTypes : currentUser.getBorrowedMoviesGenres()){//Comedy, Drama
                if (genreTypes == newGenre) {
                    isExist = true;
                }
            }
            if (!isExist){
                if (Util.allSlotsTaken(currentUser.getBorrowedMoviesGenres())){
                    reSizeGenreArray(currentUser);
                }
                else {
                    int index = Util.getAvailableIndex(currentUser.getBorrowedMoviesGenres());
                    if (index != -1){
                        currentUser.getBorrowedMoviesGenres()[index] = newGenre;
                    }
                }
            }
        }
    }

}
