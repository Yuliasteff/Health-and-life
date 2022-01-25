package by.iapsit.healthandlife.ui.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.databinding.FragmentUserDataBinding
import by.iapsit.healthandlife.db.AppDatabase
import by.iapsit.healthandlife.domain.entity.UserEntity
import by.iapsit.healthandlife.domain.entity.Gender
import by.iapsit.healthandlife.db.UserDao
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.domain.util.UserMapper
import by.iapsit.healthandlife.service.api.ApiClient
import by.iapsit.healthandlife.service.api.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate

class UserDataFragment : Fragment() {
    private val DEFAULT_EMAIL = "default@gmail.com"

    private lateinit var binding: FragmentUserDataBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_data, container, false)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        userMapper = UserMapper()

        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        binding.saveButton.setOnClickListener {
            updateUserData()
            findNavController().navigate(UserDataFragmentDirections.actionUserDataToProfile())
        }

        userDao = db.userDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUserEmail = getCurrentUserEmail()
        val userFromDb = userDao.findByEmail(currentUserEmail)
        fillUserDataFields(userFromDb)
    }

    private fun fillUserDataFields(userEntityFromDb: UserEntity) {
        binding.firstNameField.editText?.setText(userEntityFromDb.firstName)
        binding.lastNameField.editText?.setText(userEntityFromDb.lastName)
        val dateOfBirth = userEntityFromDb.dateOfBirth?.let { LocalDate.ofEpochDay(it) }
        binding.dateOfBirthField.editText?.setText(dateOfBirth.toString())
        setUserGenderField(userEntityFromDb)
        binding.medicationsField.editText?.setText(userEntityFromDb.medications)
        binding.weightField.editText?.setText(userEntityFromDb.weight?.toString())
        binding.phoneField.editText?.setText(userEntityFromDb.phoneNumber)
        binding.emailField.editText?.setText(userEntityFromDb.email)
        binding.medicationsField.editText?.setText(userEntityFromDb.medications)
    }

    private fun setUserGenderField(userEntityFromDb: UserEntity) {
        val maleCheckbox = binding.maleCheckBox
        val femaleCheckbox = binding.femaleCheckBox

        when (userEntityFromDb.gender) {
            Gender.MALE -> maleCheckbox.isChecked = true
            Gender.FEMALE -> femaleCheckbox.isChecked = true
        }
    }

    private fun updateUserData() {
        val currentUserEmail = getCurrentUserEmail()
        if (currentUserEmail != DEFAULT_EMAIL) {

            val authHeader = "Bearer ${sessionManager.getAuthToken()}"

            val userFromDb = userDao.findByEmail(currentUserEmail)
            val userToUpdate = buildUserEntity(userFromDb)

            apiClient.getUserService().updateUser(authHeader, userMapper.toUpdateRequest(userToUpdate)).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    userDao.update(userToUpdate)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun buildUserEntity(userEntityFromDb: UserEntity): UserEntity {
        val firstName = binding.firstNameField.editText?.text.toString()
        val lastName = binding.lastNameField.editText?.text.toString()
        val dateOfBirth = getDateOfBirth()
        val gender = getUserGender()
        val medications = binding.medicationsField.editText?.text.toString()
        val weight = getUserWeight()
        val phoneNumber = binding.phoneField.editText?.text.toString()
        val email = binding.emailField.editText?.text?.toString()

        return UserEntity(
            id = userEntityFromDb.id,
            login = userEntityFromDb.login,
            password = userEntityFromDb.password,
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            gender = gender,
            weight = weight,
            phoneNumber = phoneNumber,
            email = email,
            heartRate = userEntityFromDb.heartRate,
            saturation = userEntityFromDb.saturation,
            temperature = userEntityFromDb.temperature,
            medications = medications
        )
    }

    private fun getCurrentUserEmail(): String {
        return sessionManager.getCurrentUserEmail()!!
    }

    private fun getUserGender(): Gender? {
        val maleCheckbox = binding.maleCheckBox
        val femaleCheckbox = binding.femaleCheckBox

        return when {
            maleCheckbox.isChecked -> Gender.MALE
            femaleCheckbox.isChecked -> Gender.MALE
            else -> null
        }
    }

    private fun getUserWeight(): Int {
        val weightField = binding.weightField.editText?.text?.toString()
        return when {
            !weightField.isNullOrEmpty() -> weightField.toInt()
            else -> 0
        }
    }

    private fun getDateOfBirth(): Long? {
        val dateOfBirthField = binding.dateOfBirthField.editText?.text
        return try {
            LocalDate.parse(dateOfBirthField).toEpochDay()
        } catch (ex: Exception) {
            null
        }
    }
}