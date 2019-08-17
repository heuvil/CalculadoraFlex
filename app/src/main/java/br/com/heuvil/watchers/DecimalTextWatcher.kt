package br.com.heuvil.watchers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.heuvil.R
import br.com.heuvil.ui.result.ResultActivity
import kotlinx.android.synthetic.main.activity_form.*
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat

class DecimalTextWatcher(editText: EditText, val totalDecimalNumber: Int = 2) : TextWatcher {

    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)
    init { formatNumber(editTextWeakReference.get()!!.text)
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {} override fun afterTextChanged(editable: Editable) {
        formatNumber(editable) }
    private fun getTotalDecimalNumber(): String { val decimalNumber = StringBuilder()
        for (i in 1..totalDecimalNumber) {
            decimalNumber.append("0") }
        return decimalNumber.toString() }

    private fun formatNumber(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        val cleanString = editable.toString().trim().replace(" ", "")
        editText.removeTextChangedListener(this)
        val number = Math.pow(10.toDouble(), totalDecimalNumber.toDouble())
        val parsed = when (cleanString) {
            null -> BigDecimal(0)
            "" -> BigDecimal(0)
            "null" -> BigDecimal(0)
            else -> BigDecimal(cleanString.replace("\\D+".toRegex(), ""))
                .setScale(totalDecimalNumber, BigDecimal.ROUND_FLOOR) .divide(
                    BigDecimal(number.toInt()),
                    BigDecimal.ROUND_FLOOR )
        }
        val dfnd = DecimalFormat("#,##0.${getTotalDecimalNumber()}")
        val formatted = dfnd.format(parsed)
        editText.setText(formatted.replace(',', '.'))
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

}
