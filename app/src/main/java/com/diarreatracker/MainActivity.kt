package com.diarreatracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.diarreatracker.ui.FileHandler
import com.diarreatracker.ui.FreeFlowScreenManager
import com.example.diarreatracker.R
import java.security.MessageDigest


class MainActivity : ComponentActivity() {
    private lateinit var zoomableLayout: ConstraintLayout
    private val fileHandler = FileHandler(this)
    private val threadHandler = Handler(Looper.getMainLooper())
    private val scaleHolder = ScaleHolder()
    private val permissionLevel = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTeamBuilderScreen(permissionLevel)
    }

    inner class ScaleHolder{
        var scaleFactor: Float = 1.0f
    }
    private fun setLoginScreen(){
        setContentView(R.layout.login_page)

        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val pass = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {

            val hashedEmail = sha256(email.text.toString())
            val hashedPass = sha256(pass.text.toString())
//
//            thread {
//                communicationHandler.initContact()
//                val perm = communicationHandler.authenticate(hashedEmail, hashedPass)
//                threadHandler.post { permissionLevel = perm }
//                if (perm>= 0){
//                    // ON SUCCESSFUL LOGIN DO THIS
//                    Log.d("POOP", communicationHandler.getNameSuggestion("C").toString())
//                } else{
//                    threadHandler.post{
//                    val text = "Login Failed"
//                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//                    Log.d("AUTH", "AUTHENTICATION FAILED")
//                    }
//                }
//            }
        }
    }


    private fun setFreeFlowScreen(permissionLevel: Boolean) {
        setContentView(R.layout.free_flow_page)
        zoomableLayout = findViewById(R.id.zoomable_layout)
        // Initialize FreeFlowScreenManager
        val freeFlowScreenManager = FreeFlowScreenManager(this, zoomableLayout, fileHandler, scaleHolder, permissionLevel)
        freeFlowScreenManager.initialize()
    }

    private fun setTeamBuilderScreen(permissionLevel: Boolean){
        if (permissionLevel) {
            setContentView(R.layout.team_builder)
            val layout = findViewById<ConstraintLayout>(R.id.teamBuilderConstraintLayout)

            val teamBuilderScreenManager = TeamBuilderScreenManager(this, layout)
            teamBuilderScreenManager.initialize()
        }


    }

    private fun sha256(input: String): String{
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        return bytes.joinToString(""){
            "%02x".format(it)
        }
    }
}
