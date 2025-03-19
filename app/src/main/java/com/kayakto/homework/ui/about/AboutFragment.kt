package com.kayakto.homework.ui.about

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kayakto.homework.databinding.FragmentAboutBinding

/**
 * Экран "О приложении"
 */
class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textAboutDescription.text = "Приложение для управления хорошими и плохими привычками."
        binding.textAboutRights.text = "© 2025 All rights reserved"

        val githubLink = "https://github.com/kayakto"
        val textWithLink = "Created by kayakto for Doubletapp"

        val spannableString = SpannableString(textWithLink)

        val startIndex = textWithLink.indexOf("kayakto")
        val endIndex = startIndex + "kayakto".length
        spannableString.setSpan(
            URLSpan(githubLink),
            startIndex,
            endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textAboutDev.text = spannableString
        // Для перехода по ссылке
        binding.textAboutDev.movementMethod = LinkMovementMethod.getInstance()
    }
}