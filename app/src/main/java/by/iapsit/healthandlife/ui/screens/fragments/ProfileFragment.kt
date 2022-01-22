package by.iapsit.healthandlife.ui.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.databinding.FragmentProfileBinding
import by.iapsit.healthandlife.domain.model.User
import by.iapsit.healthandlife.service.api.ApiClient
import by.iapsit.healthandlife.service.api.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        binding.profileButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToMain())
        }

        binding.userDataButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToUserData())
        }

        initUserData()

        return binding.root
    }

    private fun initUserData() {

    }

    private fun getUserData() {
        val authToken = "Bearer ${sessionManager.getAuthToken()}"
        val currentUserEmail = sessionManager.getCurrentUserEmail()
        if(currentUserEmail != null) {
            apiClient.getUserService().getUser(authToken, currentUserEmail).enqueue(object :
                Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}