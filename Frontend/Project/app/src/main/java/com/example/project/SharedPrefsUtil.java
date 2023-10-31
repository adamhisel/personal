package com.example.project;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for handling operations related to shared preferences, particularly for
 * saving and retrieving user data.
 */
public class SharedPrefsUtil {
    // Constants for shared preferences file name and keys
    private static final String PREFS_NAME = "com.example.project.PREFS";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";
    private static final String KEY_USER_TYPE = "KEY_USER_TYPE";
    private static final String KEY_USER_ID = "KEY_USER_ID";

    /**
     * Saves user data to shared preferences.
     *
     * @param context     The application context used to access shared preferences.
     * @param userName    The user's name.
     * @param email       The user's email address.
     * @param phoneNumber The user's phone number.
     * @param userType    The type of the user (e.g., player, admin, etc.).
     * @param userId      The unique identifier of the user.
     */
    public static void saveUserData(Context context, String userName, String email, String phoneNumber, String userType, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Retrieves the user's name from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     * @return The user's name or an empty string if not set.
     */
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    /**
     * Retrieves the user's email address from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     * @return The user's email address or an empty string if not set.
     */
    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    /**
     * Retrieves the user's phone number from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     * @return The user's phone number or an empty string if not set.
     */
    public static String getPhoneNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE_NUMBER, "");
    }

    /**
     * Retrieves the user's type from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     * @return The user's type or an empty string if not set.
     */
    public static String getUserType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TYPE, "");
    }

    /**
     * Retrieves the user's unique identifier from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     * @return The user's unique identifier or an empty string if not set.
     */
    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    /**
     * Clears all user data from shared preferences.
     *
     * @param context The application context used to access shared preferences.
     */
    public static void clearUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
