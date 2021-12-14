package by.iapsit.healthandlife.ui.screens.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var viewModel: MainViewModel

    private var isWorking: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.profileButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainToProfile())
        }

        binding.retryButton.setOnClickListener {
            viewModel.subscribeTopics()
        }

        binding.doneButton.setOnClickListener {
            viewModel.unsubscribeTopics()
        }

        binding.powerButton.setOnClickListener {
            if (isWorking) {
                viewModel.unsubscribeTopics()
                viewModel.resetValues()
                binding.powerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.off_button))
            } else {
                viewModel.subscribeTopics()
                binding.powerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.send_result_button))
            }
            isWorking = !isWorking
        }

        return binding.root
    }
}