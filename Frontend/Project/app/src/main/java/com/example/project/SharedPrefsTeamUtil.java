package com.example.project;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for handling operations related to shared preferences, particularly for
 * saving and retrieving user data.
 */
public class SharedPrefsTeamUtil {
    // Constants for shared preferences file name and keys
    private static final String PREFS_TEAM = "com.example.project.PREFS";
    private static final String KEY_TEAM_NAME = "KEY_TEAM_NAME";
    private static final String KEY_TEAM_ID = "KEY_TEAM_ID";
    private static final String KEY_IS_COACH = "KEY_IS_COACH";
    private static final String KEY_IS_FAN = "KEY_IS_FAN";



    /**
     * this method takes in all variables to set the Team Preferences when called
     * @param context
     * @param teamName
     * @param teamId
     */
    public static void saveTeamData(Context context, String teamName, String teamId, String isCoach, String isFan){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TEAM_NAME, teamName);
        editor.putString(KEY_TEAM_ID, teamId);
        editor.putString(KEY_IS_COACH, isCoach);
        editor.putString(KEY_IS_FAN, isFan);
        editor.apply();
    }

    public static String getTeamName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TEAM_NAME, "");
    }

    public static String getTeamId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TEAM_ID, "");
    }

    public static String getIsCoach(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IS_COACH, "");
    }

    public static String getIsFan(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IS_FAN, "");
    }


    /**
     * this method clears the team data
     * @param context
     */
    public static void clearTeamData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TEAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TEAM_ID);
        editor.remove(KEY_TEAM_NAME);
        editor.remove(KEY_IS_COACH);
        editor.remove(KEY_IS_FAN);
        editor.apply();
    }
}
