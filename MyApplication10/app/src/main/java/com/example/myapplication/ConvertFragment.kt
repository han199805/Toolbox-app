package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentConvertBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
data class Unit(val text: String, val measures: List<String>)

class ConvertFragment : Fragment() {

    lateinit var binding: FragmentConvertBinding

    private fun convertValue(value: Double, fromUnit: String, toUnit: String): Double {
        return when (fromUnit to toUnit) {
            "cm" to "Inch" -> value * 0.393701
            "Inch" to "cm" -> value * 2.54
            "lb" to "kg" -> value * 0.453592
            "kg" to "lb" -> value * 2.20462
            "F" to "C" -> (value - 32) * 5 / 9
            "C" to "F" -> value * 9 / 5 + 32
            "$" to "₩" -> value * 1399.42  // 예제 값으로 환율 설정
            "₩" to "$" -> value * 0.00071
            else -> value // 같은 단위이거나 변환이 불필요한 경우
        }
    }


    private val units = listOf(
        Unit(text = "Length", measures = listOf("cm", "Inch")),
        Unit(text = "Weight", measures = listOf("lb", "kg")),
        Unit(text = "Temperature", measures = listOf("F", "C")),
        Unit(text = "Currency", measures = listOf("$", "₩"))
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_convert, container, false)
        return binding.root
    }

    private fun converting() {
        val convertInput = binding.space.text.toString()

        if (convertInput.isNotEmpty()) {
            val value = convertInput.toDouble()

            val fromUnit = binding.unit1.selectedItem?.toString()
            val toUnit = binding.unit2.selectedItem?.toString()

            if (fromUnit != null && toUnit != null) {
                val result = convertValue(value, fromUnit, toUnit)

                binding.result.text = String.format("%.2f", result)
            } else {
                binding.result.text = "Select Units"
            }

        } else {
            binding.result.text = "Enter a value"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RadioGroup의 선택에 따라 AutoCompleteTextView 업데이트
        binding.tipOption.setOnCheckedChangeListener { _, checkedId ->
            val selectedMeasures = when (checkedId) {
                R.id.Length -> units[0].measures
                R.id.Weight -> units[1].measures
                R.id.Temperature -> units[2].measures
                R.id.Currency -> units[3].measures
                else -> emptyList()
            }

            updateSpinners(selectedMeasures)
        }

        binding.calculateButton.setOnClickListener {
            converting()
        }

        binding.btntomenu.setOnClickListener{
            findNavController().navigate(R.id.action_convertFragment_to_menuFragment)
        }
    }



    private fun updateSpinners(measures: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, measures).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.unit1.adapter = adapter
        binding.unit2.adapter = adapter
    }

}