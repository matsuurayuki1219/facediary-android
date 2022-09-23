package jp.matsuura.facediary.extenstion

fun String.checkEmailValidation(): Boolean {
    return this.length >= 16
}

fun String.checkPasswordValidation(): Boolean {
    return this.length >= 8
}