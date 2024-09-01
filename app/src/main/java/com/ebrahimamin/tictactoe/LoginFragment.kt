package com.ebrahimamin.tictactoe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ebrahimamin.tictactoe.RoomFolder.UserViewModel
import com.ebrahimamin.tictactoe.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will intercept the back button press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing to block the back button
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.getPasswordByEmail(email)
                userViewModel.getUserId(email)

                userViewModel.password.observe(viewLifecycleOwner, Observer { savedPassword ->
                    userViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
                        if (password == savedPassword && userId != null) {
                            val editor = sharedPreferences.edit()
                            editor.putInt("userId", userId)
                            editor.putString("userEmail", email) // Save the email
                            editor.apply()
                            Toast.makeText(requireContext(), "Logged in successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireActivity(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    })
                })
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.registerHere.setOnClickListener {

            findNavController().apply{
                navigate(R.id.action_loginFragment_to_registerFragment)}
        }
        return binding.root

    }

}