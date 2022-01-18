package by.iapsit.healthandlife.utils

object Constants {

    const val TAG = "AndroidMqttClient"

    const val CLIENT_ID = "b099a76130704aa183abc36c1bf8a34c"
    const val SERVER_URI = "tcp://m2.wqtt.ru:5031"
    const val USER_NAME = "konik"
    const val USER_PASSWORD = "konik"

    const val TOPIC_HEARTRATE = "health&life/heartrate"
    const val TOPIC_SPO2 = "health&life/spo2"
    const val TOPIC_HEATING_TEMPERATURE = "health&life/temp_sensor"

    const val BASE_URL = "https://4f34-178-124-194-82.ngrok.io"
    const val API_PREFIX = "$BASE_URL/api/v1"
    const val LOGIN_URL = "$API_PREFIX/auth/login"
    const val REGISTRATION_URL = "$API_PREFIX/auth/registration"
}