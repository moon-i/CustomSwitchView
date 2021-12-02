package com.study.switchtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.study.switchtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.switchViewBtn.setBackground(R.color.teal_200)
        binding.switchViewBtn.setStateChangeListener(::switchChange)
    }

    private fun switchChange(isOn: Boolean) {
        if (isOn) {
            Log.i("test", "switch On")
        } else {
            Log.i("test", "switch Off")
        }
    }
}