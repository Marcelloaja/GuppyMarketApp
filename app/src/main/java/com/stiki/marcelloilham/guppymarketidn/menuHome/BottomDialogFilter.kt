package com.stiki.marcelloilham.guppymarketidn.menuHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.stiki.marcelloilham.guppymarketidn.R
import java.text.NumberFormat
import java.util.*

class BottomDialogFilter : BottomSheetDialogFragment() {

    private var selectedTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_dialog_filter, container, false)

        view?.let {
            val textViewHighToLow = it.findViewById<TextView>(R.id.textViewHighToLow)
            val textViewLowToHigh = it.findViewById<TextView>(R.id.textViewLowToHigh)

            it.findViewById<LinearLayout>(R.id.linearLayoutHighToLow).setOnClickListener {
                changeSelection(textViewHighToLow, textViewLowToHigh)
            }

            it.findViewById<LinearLayout>(R.id.linearLayoutLowToHigh).setOnClickListener {
                changeSelection(textViewLowToHigh, textViewHighToLow)
            }

            // Set OnClickListener untuk tombol tutup
            it.findViewById<ImageButton>(R.id.btnCloseDialog).setOnClickListener {
                dismiss()
            }

            val priceRangeValue = it.findViewById<TextView>(R.id.priceRangeValue)

            // observe for second type of slider
            val continuousRangeSlider: RangeSlider = it.findViewById(R.id.continuousRangeSlider)
            continuousRangeSlider.addOnChangeListener { slider, value, fromUser ->
                priceRangeValue.text = formatRupiah(slider.values[0]) + " - " + formatRupiah(slider.values[1])
            }
        }

        return view
    }

    private fun changeSelection(selected: TextView, other: TextView) {
        // Reset the previously selected TextView
        selectedTextView?.let {
            it.setBackgroundResource(R.drawable.kotak_sort)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }

        // Set the new selected TextView
        selected.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.kotak_sort_hover))
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // Update the selected TextView reference
        selectedTextView = selected

        // Reset the other TextView
        other.setBackgroundResource(R.drawable.kotak_sort)
        other.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
    }

    private fun formatRupiah(value: Float): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        format.maximumFractionDigits = 0 // Remove decimal places
        val formattedValue = format.format(value)
        return formattedValue.replace("Rp", "Rp. ")
    }
}
