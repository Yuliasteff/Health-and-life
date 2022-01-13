package by.iapsit.healthandlife.ui.screens.fragments

import android.content.Context
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
import by.iapsit.healthandlife.databinding.FragmentLoginBinding
import by.iapsit.healthandlife.ui.screens.db.entity.AppDatabase
import by.iapsit.healthandlife.ui.screens.db.entity.AppUser
import by.iapsit.healthandlife.ui.screens.db.entity.PredefinedUsers
import by.iapsit.healthandlife.ui.screens.db.entity.UserDao

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    private val CURRENT_USER_KEY = "CURRENT_USER"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.singUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToSingUp())
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

    private fun authenticateUser(login: String, password: String) {
        val userToAuth = userDao?.findByLoginAndPassword(login, password)
        if(userToAuth != null) {
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPreferences.edit()) {
                putString(CURRENT_USER_KEY, userToAuth.login)
                apply()
            }
            findNavController().navigate(LoginFragmentDirections.actionLoginToMain())
        }
    }
}