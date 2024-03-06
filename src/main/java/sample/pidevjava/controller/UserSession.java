package sample.pidevjava.controller;

import sample.pidevjava.model.User;

public  class UserSession  {
    static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}