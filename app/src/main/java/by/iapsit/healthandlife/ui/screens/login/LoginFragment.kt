package by.iapsit.healthandlife.ui.screens.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.databinding.FragmentLoginBinding
import by.iapsit.healthandlife.utils.Constants

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToMain())
        }

        return binding.root
    }
}