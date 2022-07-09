package jp.matsuura.facediary.datasource

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class FaceDiaryPreference(context: Context) {

    companion object {
        private const val PREFERENCE_NAME: String = "facediary"
    }

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)

    private val editor =
        preference.edit()

    var accessToken: String
        set(value) {
            editor.putString("accessToken", value)
            editor.commit()
        }
        get() {
            return preference.getString("accessToken", "") ?: ""
        }
}