package by.iapsit.healthandlife.ui.screens.login

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
import by.iapsit.healthandlife.databinding.FragmentLoginBinding
import by.iapsit.healthandlife.ui.screens.db.entity.AppDatabase
import by.iapsit.healthandlife.ui.screens.db.entity.AppUser
import by.iapsit.healthandlife.ui.screens.db.entity.PredefinedUsers
import by.iapsit.healthandlife.ui.screens.db.entity.UserDao
import by.iapsit.healthandlife.utils.Constants

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.singUpButton.setOnClickListener {
            val loginField = binding.loginInputField.editText
            val passwordField = binding.passwordInputField.editText
            registerUser(
                loginField?.text.toString(),
                passwordField?.text.toString()
            )
            loginField?.setText("")
            passwordField?.setText("")
        }

        binding.loginButton.setOnClickListener {
            val login: String = binding.loginInputField.editText?.text.toString()
            val password: String = binding.passwordInputField.editText?.text.toString()
            authenticateUser(login, password)
        }

        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        userDao = db?.userDao()
        PredefinedUsers.users.forEach {
            user -> userDao?.insertAll(user)
        }

        return binding.root
    }

    private fun registerUser(login: String?, password: String?) {
        if(login != null && password != null && login.isNotEmpty() && password.isNotEmpty()) {
            val userToCreate = AppUser(login, password)
            userDao?.insertAll(userToCreate)
        } else {
            val invalidInputToast = Toast.makeText(
                requireActivity().applicationContext,
                "Login and Password fields in mandatory",
                Toast.LENGTH_LONG
            )
            invalidInputToast.show()
        }

    }

    private fun authenticateUser(login: String, password: String) {
        val userToAuth = userDao?.findByLoginAndPassword(login, password)
        if(userToAuth != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginToMain())
        }
    }
}