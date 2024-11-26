package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentMenuBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MenuFragment : Fragment() {

    lateinit var binding: FragmentMenuBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMenuBinding.inflate(inflater, container, false)


        // 버튼 클릭 리스너 설정
        binding.btn1.setOnClickListener { handleButtonClick(R.id.btn1) }
        binding.btn2.setOnClickListener { handleButtonClick(R.id.btn2) }
        binding.btn3.setOnClickListener { handleButtonClick(R.id.btn3) }
        binding.btn4.setOnClickListener { handleButtonClick(R.id.btn4) }


        return binding.root

    }
    private fun handleButtonClick(buttonId: Int) {
        when (buttonId) {
            R.id.btn1 -> findNavController().navigate(R.id.action_menuFragment_to_convertFragment)
            R.id.btn2 -> findNavController().navigate(R.id.action_menuFragment_to_rulerFragment)
            R.id.btn3 -> findNavController().navigate(R.id.action_menuFragment_to_protractorFragment)
            R.id.btn4 -> findNavController().navigate(R.id.action_menuFragment_to_stopwatchFragment)
        }
    }
}