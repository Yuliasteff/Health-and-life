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
import by.iapsit.healthandlife.databinding.FragmentSingUpBinding
import by.iapsit.healthandlife.domain.dto.RegistrationRequest
import by.iapsit.healthandlife.db.AppDatabase
import by.iapsit.healthandlife.db.UserDao
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.service.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingUpFragment : Fragment() {
    private val CURRENT_USER_KEY = "CURRENT_USER"
    private val REGISTRATION_ERROR_MESSAGE = "Error occurred during registration"

    private lateinit var binding: FragmentSingUpBinding
    private lateinit var apiClient: ApiClient
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sing_up, container, false)

        binding.singUpButton.setOnClickListener {
            val login: String = binding.loginField.editText?.text.toString()
            val password: String = binding.passwordField.editText?.text.toString()
            val confirmPassword: String = binding.confirmPasswordField.editText?.text.toString()
            registerUser(login, password, confirmPassword)
        }

        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "my-db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        userDao = db.userDao()

        apiClient = ApiClient()

        return binding.root
    }

    private fun registerUser(email: String?, password: String?, confirmPassword: String?) {
        if (email != null && email.isNotEmpty() &&
            password != null && password.isNotEmpty() &&
            confirmPassword != null && confirmPassword.isNotEmpty()
            && isPasswordsEqual(password, confirmPassword)
        ) {
            apiClient.getAuthService()
                .registration(RegistrationRequest(email, password, confirmPassword)).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if(response.code() == 200) {
                            findNavController().navigate(SingUpFragmentDirections.actionSingUpToLogin())
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("Api call error", t.localizedMessage.toString())
                        Toast.makeText(requireContext(), REGISTRATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                    }
                })
        } else {
            val invalidInputToast = Toast.makeText(
                requireActivity().applicationContext,
                "Login and Password fields is mandatory",
                Toast.LENGTH_LONG
            )
            invalidInputToast.show()
        }
    }

    private fun isPasswordsEqual(password: String?, confirmPassword: String?) : Boolean {
        return password?.equals(confirmPassword)!!
    }
}