package by.iapsit.healthandlife.ui.screens.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.constants.PredefinedUsers
import by.iapsit.healthandlife.databinding.FragmentMainBinding
import by.iapsit.healthandlife.db.AppDatabase
import by.iapsit.healthandlife.db.UserDao
import by.iapsit.healthandlife.domain.dto.UpdateUserRequest
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.service.api.ApiClient
import by.iapsit.healthandlife.service.api.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    private var isWorking: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
        sessionManager = SessionManager(requireContext())
        apiClient = ApiClient()
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()

        initDefaultUsers()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.profileButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainToProfile())
        }

        binding.retryButton.setOnClickListener {
            viewModel.subscribeTopics()
        }

        binding.doneButton.setOnClickListener {
            viewModel.unsubscribeTopics()
        }

        binding.powerButton.setOnClickListener {
            if (isWorking) {
                viewModel.unsubscribeTopics()
                viewModel.resetValues()
                binding.powerButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.off_button))
            } else {
                viewModel.subscribeTopics()
                binding.powerButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.send_result_button))
            }
            isWorking = !isWorking
        }

        binding.sendResultsButton.setOnClickListener { sendResults() }

        return binding.root
    }

    private fun initDefaultUsers() {
        PredefinedUsers.users.forEach { user ->
            userDao.insertAll(user)
        }
    }

    private fun sendResults() {

        val updateRequest = buildUpdateRequest()
        val authToken = "Bearer ${sessionManager.getAuthToken()}"
        apiClient.getUserService().updateUser(authToken, updateRequest)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Toast.makeText(requireContext(), "Save succeed", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("Api call error", t.localizedMessage.toString())
                    Toast.makeText(requireContext(), "Save data error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun buildUpdateRequest(): UpdateUserRequest {
        val heartRateFieldValue = binding.heartRate.text.toString()
        val saturationFieldValue = binding.spo2.text.toString()
        val temperatureFieldValue = binding.tempSensor.text.toString()

        val heartRate = filterNumberFromString(heartRateFieldValue).toInt()
        val saturation = filterNumberFromString(saturationFieldValue).toInt()
        val temperature = filterNumberFromString(temperatureFieldValue).toDouble()

        return UpdateUserRequest(
            heartRate = heartRate,
            saturation = saturation,
            temperature = temperature
        )
    }

    private fun filterNumberFromString(stringToParse: String): String {
        return stringToParse.filter { !it.isLetter() }
            .replace("%", "")
            .replace("°", "")
    }
}