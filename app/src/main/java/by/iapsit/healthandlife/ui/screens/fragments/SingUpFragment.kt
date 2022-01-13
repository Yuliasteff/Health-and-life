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
import by.iapsit.healthandlife.databinding.FragmentSingUpBinding
import by.iapsit.healthandlife.ui.screens.db.entity.AppDatabase
import by.iapsit.healthandlife.ui.screens.db.entity.AppUser
import by.iapsit.healthandlife.ui.screens.db.entity.PredefinedUsers
import by.iapsit.healthandlife.ui.screens.db.entity.UserDao

class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    private val CURRENT_USER_KEY = "CURRENT_USER"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sing_up, container, false)

        binding.singUpButton.setOnClickListener {
            val login: String = binding.loginField.editText?.text.toString()
            val password: String = binding.passwordField.editText?.text.toString()
            registerUser(login, password)
            findNavController().navigate(SingUpFragmentDirections.actionSingUpToMain())
        }

        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        userDao = db?.userDao()
        PredefinedUsers.users.forEach { user ->
            userDao?.insertAll(user)
        }

        return binding.root
    }

    private fun registerUser(login: String?, password: String?) {
        if (login != null && password != null && login.isNotEmpty() && password.isNotEmpty()) {
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
}