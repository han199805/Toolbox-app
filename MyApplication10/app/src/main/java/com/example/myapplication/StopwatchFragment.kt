package com.example.myapplication

import android.os.SystemClock
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentStopwatchBinding
import android.widget.Chronometer
import android.widget.TextView
import androidx.navigation.fragment.findNavController

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class StopwatchFragment : Fragment() {

    private lateinit var timeTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var backButton: Button

    private var startTime = 0L
    private var elapsedTime = 0L
    private var pauseTime = 0L
    private var isRunning = false

    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            val totalTime = pauseTime + elapsedTime

            val minutes = ((totalTime / (1000 * 60)) % 60).toInt()
            val seconds = ((totalTime / 1000) % 60).toInt()
            val hundredths = ((totalTime % 1000) / 10).toInt() // 0.01초 단위

            timeTextView.text = String.format("%02d:%02d:%02d", minutes, seconds, hundredths)

            handler.postDelayed(this, 10) // 10ms마다 업데이트
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        timeTextView = view.findViewById(R.id.timeTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)
        backButton= view.findViewById(R.id.btntomenu)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = System.currentTimeMillis()
                handler.post(updateRunnable)
                isRunning = true
            }
        }

        stopButton.setOnClickListener {
            if (isRunning) {
                pauseTime += elapsedTime
                handler.removeCallbacks(updateRunnable)
                isRunning = false
            }
        }

        resetButton.setOnClickListener {
            startTime = 0L
            elapsedTime = 0L
            pauseTime = 0L
            isRunning = false
            handler.removeCallbacks(updateRunnable)
            timeTextView.text = "00:00:00"
        }

        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_stopwatchFragment_to_menuFragment)
        }

        return view
    }



}