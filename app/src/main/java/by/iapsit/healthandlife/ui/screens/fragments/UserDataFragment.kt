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
import by.iapsit.healthandlife.ui.screens.db.entity.AppDatabase
import by.iapsit.healthandlife.ui.screens.db.entity.AppUser
import by.iapsit.healthandlife.ui.screens.db.entity.UserDao

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

        val userFromDb = userDao?.findByLogin(currentUserLogin)
        if (userFromDb != null) {
            fillUserDataFields(userFromDb)
        }

    }

    private fun fillUserDataFields(userFromDb: AppUser) {
        binding.firstNameField.editText?.setText(userFromDb.firstName)
        binding.lastNameField.editText?.setText(userFromDb.lastName)
        binding.dateOfBirthField.editText?.setText(userFromDb.dateOfBirth)
        setUserGenderField(userFromDb)
        binding.weightField.editText?.setText(userFromDb.weight)
        binding.phoneField.editText?.setText(userFromDb.phoneNumber)
        binding.emailField.editText?.setText(userFromDb.email)
    }

    private fun setUserGenderField(userFromDb: AppUser) {
        val maleCheckbox = binding.maleCheckBox
        val femaleCheckbox = binding.femaleCheckBox

        when (userFromDb.gender) {
            "Male" -> maleCheckbox.isChecked = true
            "Female" -> femaleCheckbox.isChecked = true
        }
    }

    private fun updateUserData() {
        val currentUserLogin = getCurrentUserLogin()
        if (currentUserLogin != DEFAULT_LOGIN) {
            val userFromDb = userDao?.findByLogin(currentUserLogin)
            if (userFromDb != null) {
                val userToUpdate = mapInputDataToAppUser(userFromDb)
                userDao?.update(userToUpdate)
            }
        }
    }

    private fun mapInputDataToAppUser(userFromDb: AppUser): AppUser {
        val firstName = binding.firstNameField.editText?.text.toString()
        val lastName = binding.lastNameField.editText?.text.toString()
        val dateOfBirth = binding.dateOfBirthField.editText?.text.toString()
        val gender = getUserGender()
        val weight = binding.weightField.editText?.text.toString()
        val phoneNumber = binding.phoneField.editText?.text.toString()
        val email = binding.emailField.editText?.text.toString()

        return AppUser(
            id = userFromDb.id,
            login = userFromDb.login!!,
            password = userFromDb.password!!,
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            gender = gender,
            weight = weight,
            phoneNumber = phoneNumber,
            email = email
        )
    }

    private fun getCurrentUserLogin(): String {
        val sharedPreferences =
            activity?.getPreferences(Context.MODE_PRIVATE) ?: return DEFAULT_LOGIN
        return sharedPreferences.getString(CURRENT_USER_KEY, DEFAULT_LOGIN) ?: return DEFAULT_LOGIN
    }

    private fun getUserGender(): String {
        val maleCheckbox = binding.maleCheckBox
        val femaleCheckbox = binding.femaleCheckBox

        return when {
            maleCheckbox.isChecked -> maleCheckbox.text.toString()
            femaleCheckbox.isChecked -> femaleCheckbox.text.toString()
            else -> ""
        }
    }
}