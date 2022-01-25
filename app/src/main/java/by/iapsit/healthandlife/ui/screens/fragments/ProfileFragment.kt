package by.iapsit.healthandlife.ui.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.databinding.FragmentProfileBinding
import by.iapsit.healthandlife.db.AppDatabase
import by.iapsit.healthandlife.db.UserDao
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.domain.entity.Gender
import by.iapsit.healthandlife.domain.util.UserMapper
import by.iapsit.healthandlife.service.api.ApiClient
import by.iapsit.healthandlife.service.api.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userMapper: UserMapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        userDao = db.userDao()
        userMapper = UserMapper()

        binding.profileButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToMain())
        }

        binding.userDataButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToUserData())
        }

        fetchUserDataFromApi()

        return binding.root
    }

    private fun fetchUserDataFromApi() {
        val authToken = "Bearer ${sessionManager.getAuthToken()}"
        val currentUserEmail = sessionManager.getCurrentUserEmail()
        if(currentUserEmail != null) {
            apiClient.getUserService().getUser(authToken, currentUserEmail).enqueue(object :
                Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val userToDisplay = response.body()
                    if(userToDisplay != null) {
                        displayUserData(userToDisplay)
                        userDao.insertAll(userMapper.toEntity(userToDisplay))
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    println("Request: ${call.request().body}" )
                    Log.e("ERROR", t.stackTraceToString())
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                    displayUserData(getDefaultUser())
                }
            })
        }
    }

    private fun displayUserData(userToDisplay: User) {
        binding.name.text = userToDisplay.firstName
        binding.dateOfBirth.text = userToDisplay.dateOfBirth.toString()
        setGender(userToDisplay.gender)
        binding.phone.text = userToDisplay.phoneNumber
        binding.email.text = userToDisplay.email
    }

    private fun getDefaultUser(): User {
        val defaultUser = userDao.findByEmail("default@gmail.com")
        return userMapper.fromEntity(defaultUser)
    }

    private fun setGender(gender: Gender?) {
        when(gender) {
            Gender.MALE -> binding.maleCheckBox.isChecked = true
            Gender.FEMALE -> binding.femaleCheckBox.isChecked = true
            else -> {
                binding.maleCheckBox.isChecked = false
                binding.femaleCheckBox.isChecked = false
            }
        }
    }

}