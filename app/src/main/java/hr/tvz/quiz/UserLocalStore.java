package hr.tvz.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import hr.tvz.quiz.model.User;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";

    private SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("id", user.getId());
        userLocalDatabaseEditor.putString("username", user.getName());
        userLocalDatabaseEditor.putString("email", user.getEmail());
        userLocalDatabaseEditor.putInt("role", user.getRoleId());
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        int id = userLocalDatabase.getInt("id", 0);
        String username = userLocalDatabase.getString("username", "");
        String email = userLocalDatabase.getString("email", "");
        int role = userLocalDatabase.getInt("role", 1);

        return new User(id, username, email, role);
    }
}
