import data.GenreTypes;
import data.Movie;
import data.User;
import service.MovieManagment;
import service.UserManagment;
import utils.Util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static User currentUser = null;

    static MovieManagment movieManagment = new MovieManagment();
    static UserManagment userManagment = new UserManagment();

    static void showMenu() {
        System.out.println("1 - Filmleri listele");
        System.out.println("2 - Film ara");
        System.out.println("3 - Film ekle");
        System.out.println("4 - Film sil");
        System.out.println("5 - Film ödünç al");
        System.out.println("6 - Filmi iade et");
        System.out.println("7 - Kullanıcıları listele");
        System.out.println("8 - Kullanıcı hesabı ara");
        System.out.println("9 - Kullanıcı hesabı aç");
        System.out.println("10 - Kullanıcı hesabı sil");
        System.out.println("11 - Aldığınız filmleri listele.");
        System.out.println("12 - Önerilen filmleri listele.");
        System.out.println("13 - Çıkış yap.");
    }

    static void borrowOrContinue(Scanner sc) {
        System.out.println("Ödünç almak istediğiniz filim varsa ID giriniz,Ana menüye dönmek için -1'ı tuşlayınız : ");
        boolean notDone = true;
        while (notDone) {
            String id = sc.next();
            if (!Objects.equals(id, "-1")) {
                Movie movie = movieManagment.getMovieById(String.valueOf(id));
                if (movie == null)
                    System.out.println("Hatalı numara girdiniz.");
                else {
                    boolean result = movieManagment.borrowTheMovie(movie, currentUser, userManagment);
                    if (result) {
                        System.out.println("| \u001B[42mFilmi ödünç aldınız \u001B[0m|");
                        System.out.println(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                        notDone = false;
                    } else {
                        System.out.println("Film şuan uygun değil");
                        notDone = false;
                    }
                }
            } else {
                notDone = false;
                Main.showMenu();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean notExit = true;
        while (notExit) {
            while (Main.currentUser == null) {
                System.out.println("1 - Kayıt ol");
                System.out.println("2 - Giriş yap");
                System.out.print("Seçiminiz: ");
                int no = sc.nextInt();
                switch (no) {
                    case 1 -> { //KAYIT
                        boolean validEnty = false;
                        String userName;
                        String email;
                        String password;
                        do {
                            System.out.print("İsim: ");
                            userName = sc.nextLine();
                            if (!Objects.equals(userName, "")) {
                                var user = Main.userManagment.getUserByName(userName);
                                if (user != null) {
                                    System.out.println("Bu kullanıcı isminde bir hesap zaten var.");
                                } else
                                    validEnty = true;
                            } else {
                                System.out.println("Geçersiz giriş.");
                            }
                        } while (!validEnty);

                        String regex = "^(.+)@(.+)$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher;
                        do {
                            validEnty = false;
                            System.out.print("Email: ");
                            email = sc.nextLine();
                            matcher = pattern.matcher(email);
                            if (!Objects.equals(email, "") && matcher.matches()) {
                                var user = Main.userManagment.getUserByEmail(email);
                                if (user != null) {
                                    System.out.println("bu emaile ait bir kullanıcı hesabı var.");
                                } else
                                    validEnty = true;
                            } else
                                System.out.println("Geçersiz email girdiniz.");
                        } while (!validEnty);

                        do {
                            validEnty = false;
                            System.out.print("Şifre: ");
                            password = sc.nextLine();
                            if (Objects.equals(password, "")) {
                                System.out.println("Geçersiz şifre girdiniz.Tekrar giriniz");
                            } else
                                validEnty = true;
                        } while (!validEnty);


                        boolean result = Main.userManagment.addUser(new User(userName, email, password));
                        if (!result) {
                            System.out.println("Bir hata oluştu.");
                        } else {
                            System.out.println("Kullanıcı kayıt edildi");
                            currentUser = Main.userManagment.enterLibrarySystem(userName, password);
                            System.out.println("\n~~~~~~~~~~~~ Hoşgeldiniz " + currentUser.getUserName() + " ~~~~~~~~~~~~");

                            Main.showMenu();
                        }
                    }
                    case 2 -> { //GİRİŞ
                        System.out.print("İsim: ");
                        sc.nextLine();
                        String username = sc.nextLine();
                        System.out.print("Şifre: ");
                        String password = sc.nextLine();
                        User user = Main.userManagment.enterLibrarySystem(username, password);
                        if (user == null) {
                            System.out.println("! Kullanıcı ismi ya da şifre hatalı");
                        } else {
                            currentUser = user;
                            System.out.println("\n~~~~~~~~~~~~ Hoşgeldiniz " + currentUser.getUserName() + " ~~~~~~~~~~~~");

                            Main.showMenu();
                        }
                    }
                }
            }
            System.out.print("Seçiminiz (-1 ile ana menüyü görüntüleyebilirsiniz): ");
            int number = sc.nextInt();
            switch (number) {
                case 1 -> {
                    var movies = Main.movieManagment.listMovies();
                    if (movies != null) {
                        System.out.println(Main.movieManagment.listMovies());
                        Main.borrowOrContinue(sc);
                    } else {
                        System.out.println("Kayıtlı film yok.");
                    }
                }
                case 2 -> {
                    System.out.print("Film ismini giriniz: ");
                    sc.nextLine();
                    String movieTitle = sc.nextLine();
                    var movie = Main.movieManagment.getMovieByTitle(movieTitle);
                    if (movie != null) {
                        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                        System.out.println("\033[1mID : \033[0m" + movie.getId() + " - " + movie.getTitle() + "Müsaitlik durumu: " + movie.isAvailable());
                        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                        Main.borrowOrContinue(sc);
                    } else {
                        System.out.println(movieTitle + " filmi yok.");
                    }
                }
                case 3 -> {
                    //Movie Title
                    Movie movie;
                    String movieTitle;
                    do {
                        System.out.print("Filmin ismi : ");
                        sc.nextLine();
                        movieTitle = sc.nextLine();
                        movie = Main.movieManagment.getMovieByTitle(movieTitle);
                        if (movie != null) {
                            System.out.println("Bu film listemize kayıtlı.");
                        }
                    } while (movie != null);

                    //Movie Director
                    System.out.print("Filmin yönetmeni : ");
                    String movieDirector = sc.nextLine();

                    //Movie ReleaseDate
                    boolean entryValid = true;
                    String movieReleaseDate;
                    do {
                        System.out.print("Filmin yayın tarihi Yıl-Ay-Gün şeklinde : ");
                        movieReleaseDate = sc.nextLine();
                        if (!Util.isDate(movieReleaseDate, "-")) {
                            entryValid = false;
                            System.out.println("Hatalı tarih girdiniz");
                        }
                    } while (!entryValid);
                    String[] date = movieReleaseDate.split("-");
                    LocalDate movieLocalDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

                    //Movie Presentation
                    System.out.print("Filmin açıklamasını : ");
                    String moviePresentation = sc.nextLine();

                    //Movie Types
                    System.out.println("Geçerli tipler :");
                    for (int i = 0; i < GenreTypes.values().length; i++) {
                        System.out.println(i + " - " + GenreTypes.values()[i]);
                    }
                    System.out.println("Filmin tip numarasını aralarına , koyarak giriniz: ");
                    boolean validEntry;
                    String[] movieTypes;
                    GenreTypes[] genreTypes;
                    do {
                        validEntry = true;
                        String types = sc.nextLine();
                        movieTypes = types.split(",");
                        genreTypes = new GenreTypes[movieTypes.length];
                        for (int i = 0; i < movieTypes.length; i++) {
                            int enteredIndex = Integer.parseInt(movieTypes[i]);
                            if (enteredIndex > GenreTypes.values().length) {
                                validEntry = false;
                                System.out.println("Hatalı giriş yaptınız. Tekrar giriniz: ");
                                break;
                            } else {
                                genreTypes[i] = GenreTypes.values()[enteredIndex];
                            }
                        }
                    } while (!validEntry);


                    System.out.println("Filmin oyuncularını aralarına , koyarak giriniz : ");
                    String movieCast = sc.nextLine();
                    sc.nextLine();

                    String[] casts = movieCast.split(",");
                    for (int i = 0 ;i < casts.length ; i++){
                        casts[i] = casts[i].trim();
                    }


                    boolean result = movieManagment.addMovie(new Movie(movieTitle, movieLocalDate, movieDirector, casts, genreTypes));
                    if (result)
                        System.out.println("Film eklendi");
                    else
                        System.out.println("Bir sorun oluştu.");
                }
                case 4 -> {
                    System.out.print("Silmek istediğiniz filmin idsini giriniz :");
                    sc.nextLine();
                    String id = sc.nextLine();
                    var movie = movieManagment.getMovieById(id.trim());
                    if (movie == null)
                        System.out.println("Hatalı id girdiniz.");
                    else {
                        boolean result = movieManagment.deleteMovie(movie);
                        if (result)
                            System.out.println("Film silindi");
                        else {
                            System.out.println("bir hata oluştu");
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Almak istediğiniz filmin ismini giriniz : ");
                    sc.nextLine();
                    String movieTitle = sc.nextLine();
                    var movie = Main.movieManagment.getMovieByTitle(movieTitle);
                    if (movie != null && movie.isAvailable()) {
                        System.out.println("Film ödünç alındı.");
                        Main.movieManagment.borrowTheMovie(movie, currentUser, userManagment);

                    } else if (movie != null && !movie.isAvailable()) {
                        System.out.println("Film zaten ödünç alınmış.");
                    } else
                        System.out.println("Filmi bulamadık.");
                }
                case 6 -> {
                    System.out.print("İade etmek istediğiniz filmin adını giriniz: ");
                    sc.nextLine();
                    String movieTitle = sc.nextLine();
                    var movie = Main.movieManagment.getMovieByTitle(movieTitle);
                    if (movie != null) {
                        Main.movieManagment.dropTheMovie(movie);
                        System.out.println("İade işlemi başarılı.");
                    } else {
                        System.out.println("Hatalı giriş yaptınız.");
                    }
                }
                case 7 -> {
                    System.out.print("Kullanıcıları listele ");
                    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                    System.out.println(Main.userManagment.listUsers());
                    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

                }
                case 8 -> {
                    System.out.print("Kullanıcı ismi yada emaili giriniz: ");
                    sc.nextLine();
                    String userNameOrEmail = sc.nextLine();
                    var user = Main.userManagment.getUserByName(userNameOrEmail);
                    if (user == null)
                        user = Main.userManagment.getUserByEmail(userNameOrEmail);
                    if (user == null)
                        System.out.println("hatalı giriş yaptınız");
                    else {
                        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                        System.out.println(user.getId() + " - İsim: " + user.getUserName() + " - Email: " + user.getEmail() + " - Password: " + user.getPassword());
                        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
                    }

                }
                case 9 -> {
                    boolean validEnty = false;
                    String userName;
                    String email;
                    do {
                        System.out.print("İsim: ");
                        sc.nextLine();
                        userName = sc.nextLine();
                        var user = Main.userManagment.getUserByName(userName);
                        if (user != null) {
                            System.out.println("bu kullanıcı isminde bir hesap zaten var.");
                        } else
                            validEnty = true;
                    } while (!validEnty);

                    do {
                        validEnty = false;
                        System.out.print("Email: ");
                        email = sc.nextLine();
                        var user = Main.userManagment.getUserByEmail(email);
                        if (user != null) {
                            System.out.println("bu emaile ait bir kullanıcı hesabı var.");
                        } else
                            validEnty = true;
                    } while (!validEnty);

                    System.out.print("Şifre: ");
                    String password = sc.nextLine();

                    boolean result = Main.userManagment.addUser(new User(userName, email, password));
                    if (!result) {
                        System.out.println("Bir hata oluştu.");
                    } else {
                        System.out.println("Kullanıcı kayıt edildi");
                    }
                }
                case 10 -> {
                    System.out.print("Kullanıcı ismiya da emaili giriniz:");
                    sc.nextLine();
                    String userNameOrEmail = sc.nextLine();
                    var user = Main.userManagment.getUserByName(userNameOrEmail);
                    if (user == null)
                        user = Main.userManagment.getUserByEmail(userNameOrEmail);
                    if (user == null)
                        System.out.println("hatalı giriş yaptınız");
                    else {
                        boolean result = Main.userManagment.deleteUser(user);
                        if (result)
                            System.out.println("Hesap silindi");
                        else
                            System.out.println("Bir sorun oluştu.");
                    }
                }
                case 11 -> {
                    System.out.println("Aldığınız filmlerin listesi: ");
                    System.out.println(userManagment.borrowedMovieList(currentUser));

                }
                case 12 -> {
                    System.out.println("Önerilen filmlerin listesi: ");
                    String[][] movies = movieManagment.suggestMovieList(currentUser);
                    if (movies.length != 0) {

                        for (int row = 0; row < movies.length; row++) {
                            if (movies[row][0] != null) {
                                System.out.println("\033[1mUYUM: \033[0m" + movies[row][1] + "\033[1m   ID :\033[0m" + movies[row][0] + "\033[1m   TITLE : \033[0m" + movieManagment.getMovieById(movies[row][0]).getTitle());
                            }
                        }
                        Main.borrowOrContinue(sc);
                    }
                }
                case 13 -> {
                    System.out.println("Hoşçakalın.");
                    notExit = false;
                }
                case -1 ->
                    Main.showMenu();
            }
        }

    }
}