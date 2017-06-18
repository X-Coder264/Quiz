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
        userLocalDatabaseEditor.putInt("role_id", user.getRoleId());
        userLocalDatabaseEditor.putInt("title_id", user.getTitleId());
        userLocalDatabaseEditor.putInt("course_id", user.getCourseId());
        userLocalDatabaseEditor.putInt("semester", user.getSemester());
        userLocalDatabaseEditor.putString("image", user.getImage());
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
        int role_id = userLocalDatabase.getInt("role_id", 1);
        int title_id = userLocalDatabase.getInt("title_id", 1);
        int course_id = userLocalDatabase.getInt("course_id", 1);
        int semester = userLocalDatabase.getInt("semester", 1);
        String image = userLocalDatabase.getString("image", "");

        return new User(id, username, email, role_id, title_id, course_id, semester, image);
    }
}
