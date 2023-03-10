package com.example.project3.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project3.Completion
import com.example.project3.constants.Constants
import com.example.project3.databinding.FragmentSignUpBinding
import com.example.project3.models.User
import com.example.project3.repo.Repository
import com.example.project3.uiComponents.ProgressButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragmentViewModel(private val repo : Repository) : ViewModel() {
    val email = MutableLiveData("")
    val pass = MutableLiveData("")

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var button: ProgressButton

    fun setUi(it: FragmentSignUpBinding, t: ProgressButton) {
        binding = it
        button = t
    }

    fun signUp(comp: Completion) {
        button.activate()
        if (email.value.isNullOrBlank()) {
            button.deactivate()
            binding.email.error = "Email needed"
            return
        } else if (pass.value.isNullOrBlank()) {
            button.deactivate()
            binding.pass.error = "password needed"
            return
        } else if (pass.value!!.length < 6) {
            button.deactivate()
            binding.pass.error = "6 chars needed"
            return
        } else {
            Firebase.auth
                .createUserWithEmailAndPassword(email.value!!, pass.value!!)
                .addOnSuccessListener {
                    repo.authSaveUserData(it,comp,email.value!!.trim(),pass.value!!.trim(),button,binding)
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
        }

    }


    fun getPass():LiveData<String>{
        return pass
    }
}