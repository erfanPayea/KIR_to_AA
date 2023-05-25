package controller;

import model.User;
import model.game.Settings;
import view.enums.HotKeys;
import view.enums.Message;


public class Editor {
    private final User currentUser;

    public Editor() {
        this.currentUser = User.getCurrentUser();
    }


    public Message saveProfileChanges(String username, String password) { // todo : avatar
        if (currentUser.getPassword() == null)
            return Message.GUEST_PROFILE;

        if (password.isEmpty() && username.isEmpty())
            return Message.ALL_EMPTY;

        if (!username.isEmpty()) {
            if (username.length() < 4)
                return Message.LOW_USERNAME;
            if (User.getUserByUsername(username) != null)
                return Message.USERNAME_EXISTS;
            if (!password.isEmpty() && password.length() < 4)
                return Message.LOW_PASSWORD;

            currentUser.setUsername(username);
        }
        if (!password.isEmpty()) {
            if (password.length() < 4)
                return Message.LOW_PASSWORD;
            currentUser.setPassword(password);
        }

        User.saveToDatabase(currentUser);
        return Message.CHANGE_SUCCESS;
    }

    public Message saveSettingsChanges(String mapName, String level, String ballsNumber, boolean isMute, String shootKey,
                           String freezeKey, String leftKey, String rightKey) {
        Settings currentSettings = currentUser.getSettings();
        if (mapName == null && level == null && ballsNumber == null && HotKeys.SHOOT.equals(shootKey)
                && HotKeys.FREEZE.equals(freezeKey) && HotKeys.LEFT.equals(leftKey) && HotKeys.RIGHT.equals(rightKey))
            return Message.ALL_EMPTY;

        currentSettings.setMap(mapName);
        currentSettings.setLevel(level);
        currentSettings.setBallNumbers(ballsNumber);
        currentSettings.setMute(isMute);

        HotKeys.SHOOT.setKeyName(shootKey);
        HotKeys.FREEZE.setKeyName(freezeKey);
        HotKeys.LEFT.setKeyName(leftKey);
        HotKeys.RIGHT.setKeyName(rightKey);

        User.saveToDatabase(currentUser);
        return Message.CHANGE_SUCCESS;
    }

}
