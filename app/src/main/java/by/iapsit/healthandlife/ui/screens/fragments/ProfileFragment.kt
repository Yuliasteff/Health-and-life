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

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.profileButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToMain())
        }

        binding.userDataButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileToUserData())
        }

        return binding.root
    }
}