package controller;

import model.User;
import view.enums.Message;

public class LoginMenuController {
    public Message login(String username, String password, boolean stayLoggedIn) {
        if (username.isEmpty() || password.isEmpty())
            return Message.EMPTY_FIELD;

        User user = User.getUserByUsername(username);
        if (user == null)
            return Message.NOT_MATCH;

        if (user.isPasswordIncorrect(password))
            return Message.NOT_MATCH;

        User.setCurrentUser(user);

        if (stayLoggedIn)
            User.setStayLoggedIn(user);

        return null;

    }

    public Message register(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return Message.EMPTY_FIELD;

        if (username.length() < 4)
            return Message.LOW_USERNAME;
        else if (User.getUserByUsername(username) != null)
            return Message.USERNAME_EXISTS;
        else if (password.length() < 4)
            return Message.LOW_PASSWORD;
        else {
            new User(username, password);
            return Message.REGISTER_SUCCESS;
        }
    }

    public Message playAsGuest(boolean stayLoggedIn) {
        if (stayLoggedIn)
            return Message.GUEST_ERROR;

        new User();
        return null;
    }
}