package com.repit.v2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.repit.v2.utils.InterviewerManager

class SetupActivity : AppCompatActivity() {

    private lateinit var interviewerManager: InterviewerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        interviewerManager = InterviewerManager(this)

        // If setup is already complete, go to Selector
        if (interviewerManager.isSetupComplete()) {
            startActivity(Intent(this, FormSelectorActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_setup)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etSurname = findViewById<TextInputEditText>(R.id.etSurname)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val surname = etSurname.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            interviewerManager.saveInterviewer(name, surname, email)
            
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show()
            
            startActivity(Intent(this, FormSelectorActivity::class.java))
            finish()
        }
    }
}
