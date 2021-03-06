package com.joaoricardi.poc_bar_code_android

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.joaoricardi.poc_bar_code_android.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ITF)
        options.setPrompt("Escanear")
        options.setCameraId(0) // Use a specific camera of the device

        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        btn = binding.btnCallToAction

        btn.setOnClickListener {
            //chamarOPluginDeFoto()
            //barcodeLauncher.launch(ScanOptions())
            onButtonClick()
        }

        binding.askForPermissionBtn.setOnClickListener {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                1)
        }

    }

    private fun chamarOPluginDeFoto() {
        btn.visibility = View.GONE

    }

    override fun onResume() {
        checkPermission()
        super.onResume()
    }


    override fun onStart() {
        checkPermission()
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            1)
        super.onStart()
    }

    private fun checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                .PERMISSION_GRANTED){
            Toast.makeText(this, "Voc?? meu deu permsis??o =')", Toast.LENGTH_LONG ).show()
            binding.askForPermissionBtn.visibility = View.GONE
            btn.visibility = View.VISIBLE
        } else {
            btn.visibility = View.INVISIBLE
        }
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        print(result.toString())
    }

    // Launch
    fun onButtonClick() {
        barcodeLauncher.launch(ScanOptions())
    }

}

//O m??todo setResultHandler() requer uma inst??ncia de ZXingScannerView.ResultHandler como argumento. Al??m do mais, segundo a documenta????o, o lugar ideal de invoca????o deste m??todo ?? no onResume() do fragmento ou atividade em uso.
//
//Se o m??todo resumeCameraPreview() n??o for invocado posteriormente a leitura de algum c??digo, a tela de leitura fica travada. resumeCameraPreview() tamb??m espera uma inst??ncia de ZXingScannerView.ResultHandler como argumento.