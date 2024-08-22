package com.example.test3

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.test3.databinding.ActivityMovementBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator


class MovementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovementBinding
    private lateinit var user: FirebaseAuth
    private lateinit var scanbtn: MaterialButton
    private lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMovementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        if (user.currentUser != null) {
            user.currentUser?.let {
                binding.tvUserEmail.text = it.email
            }
        }
        scanbtn=findViewById(R.id.scanbtn)
        resultView=findViewById(R.id.resultView)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        scanbtn.setOnClickListener {
                val intentIntegrator = IntentIntegrator(this)
                intentIntegrator.setOrientationLocked(false)
                intentIntegrator.setPrompt("Scan QR Code")
                intentIntegrator.setDesiredBarcodeFormats(
                    IntentIntegrator.QR_CODE,
                    IntentIntegrator.DATA_MATRIX,
                    IntentIntegrator.PDF_417
                )
                intentIntegrator.initiateScan()
        }
        binding.backbtn.setOnClickListener {
            val intent = Intent(this, OptionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents = intentResult.contents
            if (contents != null) {
                resultView.text = intentResult.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (intentResult != null) {
            if (intentResult.contents != null) {
                val scannedData = intentResult.contents
                val scannedDataText = findViewById<TextView>(R.id.resultView)
                scannedDataText.text = scannedData
            }
        }
    }
}