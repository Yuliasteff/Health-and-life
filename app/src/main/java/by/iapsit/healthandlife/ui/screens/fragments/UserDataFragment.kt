package by.iapsit.healthandlife.ui.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.time.LocalDate

class UserDataFragment : Fragment() {
    private val CURRENT_USER_KEY = "CURRENT_USER"
    private val DEFAULT_LOGIN = "default_login"

    private lateinit var binding: FragmentUserDataBinding
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_data, container, false)

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

        userDao = db?.userDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val currentUserLogin = getCurrentUserLogin()

        val userFromDb = userDao?.findByEmail(currentUserLogin)
        if (userFromDb != null) {
            fillUserDataFields(userFromDb)
        }

    }

    private fun fillUserDataFields(userEntityFromDb: UserEntity) {
        binding.firstNameField.editText?.setText(userEntityFromDb.firstName)
        binding.lastNameField.editText?.setText(userEntityFromDb.lastName)
        val dateOfBirth = userEntityFromDb.dateOfBirth?.let { LocalDate.ofEpochDay(it) }
        binding.dateOfBirthField.editText?.setText(dateOfBirth.toString())
        setUserGenderField(userEntityFromDb)
        binding.weightField.editText?.setText(userEntityFromDb.weight!!)
        binding.phoneField.editText?.setText(userEntityFromDb.phoneNumber)
        binding.emailField.editText?.setText(userEntityFromDb.email)
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
        val currentUserLogin = getCurrentUserLogin()
        if (currentUserLogin != DEFAULT_LOGIN) {
            val userFromDb = userDao?.findByEmail(currentUserLogin)
            if (userFromDb != null) {
                val userToUpdate = mapInputDataToAppUser(userFromDb)
                userDao?.update(userToUpdate)
            }
        }
    }

    private fun mapInputDataToAppUser(userEntityFromDb: UserEntity): UserEntity {
        val firstName = binding.firstNameField.editText?.text.toString()
        val lastName = binding.lastNameField.editText?.text.toString()
        val dateOfBirth = binding.dateOfBirthField.editText?.text.toString().toLong()
        val gender = getUserGender()
        val weight = binding.weightField.editText?.text.toString().toInt()
        val phoneNumber = binding.phoneField.editText?.text.toString()
        val email = binding.emailField.editText?.text.toString()

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
            temperature = userEntityFromDb.temperature
        )
    }

    private fun getCurrentUserLogin(): String {
        val sharedPreferences =
            activity?.getPreferences(Context.MODE_PRIVATE) ?: return DEFAULT_LOGIN
        return sharedPreferences.getString(CURRENT_USER_KEY, DEFAULT_LOGIN) ?: return DEFAULT_LOGIN
    }

    private fun getUserGender(): Gender {
        val maleCheckbox = binding.maleCheckBox
        val femaleCheckbox = binding.femaleCheckBox

        return when {
            maleCheckbox.isChecked -> Gender.MALE
            femaleCheckbox.isChecked -> Gender.MALE
            else -> Gender.EMPTY
        }
    }
}