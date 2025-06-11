package com.diarreatracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.diarreatracker.ui.FileHandler
import com.diarreatracker.ui.FreeFlowScreenManager
import com.example.diarreatracker.R
import kotlinx.coroutines.launch
import java.security.MessageDigest


class MainActivity : ComponentActivity() {
    private lateinit var zoomableLayout: FrameLayout
    private val fileHandler = FileHandler(this)
    private val threadHandler = Handler(Looper.getMainLooper())
    private val scaleHolder = ScaleHolder()
    private val permissionLevel = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.initialize(this)
        setFreeFlowScreen(true)
        //setLoginScreen()
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

            val userName = email.text.toString()
            val hashedPass = sha256(pass.text.toString())

            if (userName.isEmpty() || hashedPass.isEmpty()){
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try{
                    val tokenResponse = ApiClient.login(userName, hashedPass)
                    val permissionLevel = ApiClient.getPermissions().permissions.toSet() //Allows O(1) lookup instead of O(n)
                    Log.d("AUTH", tokenResponse.toString() + permissionLevel.toString())
                    setFreeFlowScreen("EditKennel" in permissionLevel)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    private fun setFreeFlowScreen(permissionLevel: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.free_flow_page)
        zoomableLayout = findViewById(R.id.zoomable_content)
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
