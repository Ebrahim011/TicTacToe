package com.ebrahimamin.tictactoe

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebrahimamin.tictactoe.R
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.User
import com.ebrahimamin.tictactoe.RoomFolder.UserViewModel
import com.ebrahimamin.tictactoe.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Toggle password visibility on long click
        binding.etRegisterPassword.setOnLongClickListener {
            togglePasswordVisibility(binding.etRegisterPassword)
            true
        }
        binding.etRegisterConfirmPassword.setOnLongClickListener {
            togglePasswordVisibility(binding.etRegisterConfirmPassword)
            true
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val userName = binding.etUserName.text.toString()
            val confirmPassword = binding.etRegisterConfirmPassword.text.toString()

            // Regex patterns for validation
            val emailRegex = Regex("^[a-zA-Z0-9_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$")
            val userNameRegex = Regex("^[a-zA-Z_]+$")

            // Clear previous error messages
            binding.tvEmailHelper.visibility = View.GONE
            binding.tvPasswordHelper.visibility = View.GONE
            binding.tvConfirmPasswordHelper.visibility = View.GONE

            // Input validation
            when {
                userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                !email.matches(emailRegex) -> {
                    binding.tvEmailHelper.text = "Please enter a valid email address."
                    binding.tvEmailHelper.visibility = View.VISIBLE
                }
                !password.matches(passwordRegex) -> {
                    binding.tvPasswordHelper.text = "Password must be 6-12 characters, including at least one uppercase letter, one lowercase letter, and one number."
                    binding.tvPasswordHelper.visibility = View.VISIBLE
                }
                password != confirmPassword -> {
                    binding.tvConfirmPasswordHelper.text = "Confirm Password does not match the Password."
                    binding.tvConfirmPasswordHelper.visibility = View.VISIBLE
                    binding.etRegisterPassword.text.clear()
                    binding.etRegisterConfirmPassword.text.clear()
                }
                userViewModel.checkIfEmailExistsBoolean(email) -> {
                    Toast.makeText(requireContext(), "Email already exists", Toast.LENGTH_SHORT).show()
                    binding.etRegisterEmail.text.clear()
                    binding.etRegisterPassword.text.clear()
                    binding.etUserName.text.clear()
                    binding.etRegisterConfirmPassword.text.clear()
                }
                else -> {
                    val user = User(
                        userEmail = email,
                        userPassword = password,
                        userName = userName
                    )
                    userViewModel.addAccount(user)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }

        binding.loginHere.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }

    // Function to toggle password visibility
    private fun togglePasswordVisibility(editText: EditText) {
        if (editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        editText.setSelection(editText.text.length)
    }
}
