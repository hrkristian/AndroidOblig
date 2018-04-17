package xyz.robertsen.androidoblig;

import xyz.robertsen.androidoblig.Models.User;

/**
 * Central repository for the various states in the app, in order to
 * be accessible everywhere.
 * Groundwork for future Facade-pattern.
 */
public class AppStates {


    /* User States */
    static boolean isAuthenticated = false;
    static User authenticatedUser = null;
    /* User States */

    /* MainActivity States */
    static boolean actionViewVisible = false;
    /* MainActivity States */

    /* UserDialog States */
    static boolean userLoginDialogVisible = false;
    static boolean userSettingsDialogVisible = false;
    /* UserDialog States */

    static void setAuthenticatedUser(User user) {
        if (authenticatedUser == null)
            authenticatedUser = user;
        else
            throw new IllegalStateException("A user is already authenticated");
    }
}
