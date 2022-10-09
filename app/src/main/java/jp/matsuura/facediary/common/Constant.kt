package jp.matsuura.facediary.common

object Constant {

    const val BASE_URL: String = "https://facediary-api.herokuapp.com"
    // ErrorCode

    // Common //
    // バリデーションエラー
    const val VALIDATION_ERROR: String = "ES00_001"
    // ユニークキー（トークンなど）がDB上で重複した場合
    const val DUPLICATE_KEY: String = "ES00_002"
    // DBエラー
    const val DB_ERROR: String = "ES00_003"
    // トークン有効期限切れ
    const val TOKEN_EXPIRED: String = "ES00_004"

    // Authentication //
    // ユーザが存在しない場合
    const val NOT_USER_EXIST: String = "ES01_001"
    // パスワードが間違っている場合
    const val PASSWORD_ERROR: String = "ES01_002"
    // メール認証が済んでない場合
    const val MAIN_NOT_VERIFIED: String = "ES01_003"
    // ユーザが既に存在している場合
    const val USER_ALREADY_EXISTED: String = "ES01_004"
    // 検証用トークンが間違っている場合
    const val ES01_005: String = "ES01_005"
    // パスワードリセット用トークンが間違っている場合
    const val ES01_006: String = "ES01_006"

    // Others //
    // 不明なエラー
    const val ES99_001: String = "ES99_001"

}