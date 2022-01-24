package by.iapsit.healthandlife.ui.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.iapsit.healthandlife.constants.Constants
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val SPO2_INIT_VALUE = "0%"
        const val HEART_RATE_INIT_VALUE = "0bpm"
        const val TEMP_INIT_VALUE= "0°C"
    }

    private val mqttClient by lazy {
        MqttAndroidClient(
            (getApplication() as Application).applicationContext,
            Constants.SERVER_URI,
            Constants.CLIENT_ID
        )
    }

    private val _heartRate = MutableLiveData(HEART_RATE_INIT_VALUE)
    val heartRate: LiveData<String>
        get() = _heartRate

    private val _spo2 = MutableLiveData(SPO2_INIT_VALUE)
    val spo2: LiveData<String>
        get() = _spo2

    private val _temp = MutableLiveData(TEMP_INIT_VALUE)
    val temp: LiveData<String>
        get() = _temp


    init {
        connect()
    }

    private fun connect() {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(Constants.TAG, "Connection lost ${cause.toString()}")
                connect()
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(Constants.TAG, "Receive message: ${message.toString()} from topic: $topic")
                with(Constants) {
                    when (topic) {
                        TOPIC_HEARTRATE -> _heartRate.postValue("${message.toString()} bpm")
                        TOPIC_SPO2 -> _spo2.postValue("${message.toString()} %")
                        TOPIC_HEATING_TEMPERATURE -> _temp.postValue("${message.toString()} °C")
                    }
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
        val options = MqttConnectOptions().apply {
            userName = Constants.USER_NAME
            password = Constants.USER_PASSWORD.toCharArray()
        }
        mqttClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(Constants.TAG, "Connection success")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                connect()
            }
        })
    }

    private fun subscribe(topic: String, qos: Int = 1) {
        mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(Constants.TAG, "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(Constants.TAG, "Failed to subscribe $topic")
            }
        })
    }

    private fun unsubscribe(topic: String) {
        mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(Constants.TAG, "Unsubscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(Constants.TAG, "Failed to unsubscribe $topic")
            }
        })
    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        val message = MqttMessage().apply {
            payload = msg.toByteArray()
            this.qos = qos
            isRetained = retained
        }
        mqttClient.publish(topic, message, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(Constants.TAG, "$msg published to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(Constants.TAG, "Failed to publish $msg to $topic")
            }
        })
    }

    private fun disconnect() {
        mqttClient.disconnect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(Constants.TAG, "Disconnected")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(Constants.TAG, "Failed to disconnect")
            }
        })
    }

    fun subscribeTopics() {
        with (Constants) {
            subscribe(TOPIC_HEARTRATE)
            subscribe(TOPIC_SPO2)
            subscribe(TOPIC_HEATING_TEMPERATURE)
        }
    }

    fun unsubscribeTopics() {
        with (Constants) {
            unsubscribe(TOPIC_HEARTRATE)
            unsubscribe(TOPIC_SPO2)
            unsubscribe(TOPIC_HEATING_TEMPERATURE)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    fun resetValues() {
        _heartRate.postValue(HEART_RATE_INIT_VALUE)
        _spo2.postValue(SPO2_INIT_VALUE)
        _temp.postValue(TEMP_INIT_VALUE)
    }
}