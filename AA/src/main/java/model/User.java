package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.game.Settings;
import model.game.enums.Map;

public class User {
    private static ArrayList<User> users;
    private static User currentUser;
    private static final Gson gson = new Gson();
    private String username;
    private String password;
    private String avatar;
    private final Settings settings;
    private int lastGameScore;
    private final int[] highScores;
    public boolean hasWinLastGame;

    static {
        users = new ArrayList<>();
    }

    {
        Random random = new Random();
        int randomNumber = random.nextInt(1, 4);
        this.avatar = String.valueOf(randomNumber);

        this.highScores = new int[4];
        Arrays.fill(highScores, 0);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lastGameScore = 0;
        this.settings = new Settings(2, 20, Map.getMapByName("Space"), false);
        users.add(this);
        User.saveUsersToFile();
    }

    public User() {
        this.username = "guest";
        this.lastGameScore = 0;
        this.settings = new Settings(2, 20, Map.getMapByName("Space"), false);
        User.currentUser = this;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.username.equals(username))
                return user;
        }
        return null;
    }

    public static void remove() {
        users.remove(currentUser);
        currentUser = null;
        saveToDatabase(null);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public int[] getHighScores() {
        return this.highScores;
    }

    public int getLastGameScore() {
        return lastGameScore;
    }

    public boolean isPasswordIncorrect(String password) {
        return !this.password.equals(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLastGameScore(int lastGameScore) {
        this.lastGameScore = lastGameScore;

        if (lastGameScore > this.highScores[settings.getLevel().getNumber()])
            this.highScores[settings.getLevel().getNumber()] = lastGameScore;

        if (lastGameScore > highScores[0])
            this.highScores[0] = lastGameScore;
    }

    // database :

    public static void loadUsersFromFile() {
        String filePath = "./src/main/resources/database/userDatabase.json";
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            ArrayList<User> createdUsers = gson.fromJson(json, new TypeToken<List<User>>() {
            }.getType());

            if (createdUsers != null) {
                users = createdUsers;
            }
        } catch (IOException ignored) {
            File file = new File(filePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // dataBase :
    public static void saveToDatabase(User user) {
        if (User.loadStayLoggedIn() != null) {
            System.out.println(user.getSettings().getMap().getName());
            User.setStayLoggedIn(user);
        }

        User.saveUsersToFile();
    }

    public static User loadStayLoggedIn() {
        String filePath = "./src/main/resources/database/stayLoggedIn.json";
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));

            return gson.fromJson(json, new TypeToken<User>() {
            }.getType());

        } catch (IOException ignored) {
            File file = new File(filePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }

    public static void setStayLoggedIn(User loggedInUser) {
        User.currentUser = loggedInUser;
        String filePath = "./src/main/resources/database/stayLoggedIn.json";
        saveToFile(filePath, gson.toJson(loggedInUser));
    }

    public static void saveUsersToFile() {
        String filePath = "./src/main/resources/database/userDatabase.json";
        saveToFile(filePath, gson.toJson(users));
    }

    private static void saveToFile(String filePath, String s) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(s);
            fileWriter.close();
        } catch (IOException ignored) {
            File file = new File(filePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
