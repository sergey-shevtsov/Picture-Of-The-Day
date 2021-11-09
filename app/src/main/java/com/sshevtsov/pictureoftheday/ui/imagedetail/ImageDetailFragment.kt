package com.sshevtsov.pictureoftheday.ui.imagedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import com.sshevtsov.pictureoftheday.databinding.FragmentImageDetailBinding

class ImageDetailFragment : Fragment() {
    companion object {
        const val IMAGE_URL_EXTRA = "IMAGE_URL"
        fun newInstance(bundle: Bundle): ImageDetailFragment {
            return ImageDetailFragment().also {
                it.arguments = bundle
            }
        }
    }

    private var _binding: FragmentImageDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments?.getString(IMAGE_URL_EXTRA)?.let { url ->
            binding.imageView.load(url) {
                lifecycle(this@ImageDetailFragment)
                listener(
                    onSuccess = { _, _ ->
                        binding.loadingInclude.loadingLayout.visibility = View.GONE
                    }
                )
            }
        }
    }
}