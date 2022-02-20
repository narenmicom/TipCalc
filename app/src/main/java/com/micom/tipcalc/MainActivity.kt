package com.micom.tipcalc

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etbill: EditText
    private lateinit var tipseekbar: SeekBar
    private lateinit var tvtipbar: TextView
    private lateinit var tvtipans: TextView
    private lateinit var tvtotalans: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etbill = findViewById(R.id.etbill)
        tipseekbar = findViewById(R.id.tipseekbar)
        tvtipbar = findViewById(R.id.tvtipbar)
        tvtipans = findViewById(R.id.tvtipans)
        tvtotalans = findViewById(R.id.tvtotalans)


        tipseekbar.progress = INITIAL_TIP_PERCENT
        tvtipbar.text = "$INITIAL_TIP_PERCENT%"
        tipdescription(INITIAL_TIP_PERCENT)
        tipseekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                Log.i(TAG, "onProgressChanged $p1")
                tvtipbar.text = "$p1%"
                computeTipAndTotal()
                tipdescription(p1)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        etbill.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                Log.i(TAG, "afterTextChanges $p0")
                computeTipAndTotal()

            }

        }
        )





    }

    private fun tipdescription(tip: Int) {
        val tipdesc = when(tip){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Excellent"
            else -> "Amazing"
        }
        tvtipdesc.text = tipdesc
        val color = ArgbEvaluator().evaluate(
            tip.toFloat() / tipseekbar.max,
            ContextCompat.getColor(this,R.color.worst_tip),
            ContextCompat.getColor(this,R.color.best_tip)
        ) as Int
        tvtipdesc.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etbill.text.isEmpty()){
            tvtipans.text = ""
            tvtotalans.text = ""
            return
        }
        // Get Values
        val billAmount = etbill.text.toString().toDouble()
        val tippercent = tipseekbar.progress
        // calculate
        val tipans = billAmount * tippercent/100
        val totalans = billAmount + tipans
        // Changing to UI
        tvtipans.text = "%.2f".format(tipans)
        tvtotalans.text = "%.2f".format(totalans)
    }
}