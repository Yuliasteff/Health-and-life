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
import by.iapsit.healthandlife.databinding.FragmentLoginBinding
import by.iapsit.healthandlife.domain.dto.AuthenticationRequest
import by.iapsit.healthandlife.domain.dto.AuthenticationResponse
import by.iapsit.healthandlife.db.AppDatabase
import by.iapsit.healthandlife.db.UserDao
import by.iapsit.healthandlife.service.api.ApiClient
import by.iapsit.healthandlife.service.api.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    companion object {
        val AUTH_ERROR_MESSAGE = "Invalid email or password"
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao


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

        userDao = db.userDao()

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        return binding.root
    }

    private fun authenticateUser(email: String, password: String) {
        apiClient.getAuthService().login(AuthenticationRequest(email, password))
            .enqueue(object : Callback<AuthenticationResponse> {
                override fun onResponse(
                    call: Call<AuthenticationResponse>,
                    response: Response<AuthenticationResponse>
                ) {
                    val authResponseBody = response.body()
                    if(authResponseBody != null) {
                        sessionManager.saveAuthToken(authResponseBody.token)
                        sessionManager.saveCurrentUserEmail(email)
                        findNavController().navigate(LoginFragmentDirections.actionLoginToMain())
                    }
                }

                override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                    Log.e("Api call error", t.localizedMessage.toString())
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })

    }
}