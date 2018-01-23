package ru.kabylin.androidarchexample.systems.authorization.services

import io.reactivex.Single
import ru.kabylin.androidarchexample.services.Service
import ru.kabylin.androidarchexample.credentials.Credentials

/**
 * Сервис регистрации пользователя.
 */
interface RegistrationService : Service {
    /**
     * Запрос на регистрацию пользователя с телефоном [phone].
     *
     * @return Секретный код пользователя.
     */
    fun requestRegistration(phone: String): Single<out String>

    /**
     * Завершение регистрации пользователя.
     * Пользователю назначается пароль [password] с помощью
     * которого он может войти в приложение и код проверки пользователя [secretCode].
     * Также проверяется код активации [activationCode], который приходит на телефон
     *
     * @return Подпись для доступ к методам.
     */
    fun finishRegistration(
        password: String,
        secretCode: String,
        activationCode: String
    ): Single<out Credentials>
}
