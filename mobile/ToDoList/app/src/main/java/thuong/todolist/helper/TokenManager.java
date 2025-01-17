package thuong.todolist.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREF_NAME = "AppPrefer";
    private static final String KEY_TOKEN = "JWT_TOKEN";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        if (token != null && !token.isEmpty()) {
            sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
        }
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply();
    }
}
