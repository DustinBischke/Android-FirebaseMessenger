package ca.bischke.firebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        text_already_registered.setOnClickListener {
            buttonRegisterClick()
        }
    }

    private fun buttonRegisterClick() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
