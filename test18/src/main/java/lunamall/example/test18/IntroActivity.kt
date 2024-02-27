package lunamall.example.test18

import android.content.Intent
import android.os.Bundle
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity
import lunamall.example.test18.databinding.ActivityIntroBinding


class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    private val SPLASH_TIMEOUT:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        Handler().postDelayed(Runnable {
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIMEOUT)

    }


}