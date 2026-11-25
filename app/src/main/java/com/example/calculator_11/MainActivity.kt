package com.example.calculator_11

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //ประกาศตัวแปร
    private lateinit var display: TextView //แสดงผลลัพธ์ตัวแรก
    private var current = "" //ค่าที่กำลังพิมพ์อยู่
    private var operator = "" //เครื่องหมายที่เลือก เช่น +,-,*,/
    private var firstNumber = 0.0 //ตัวเลขแรก
    private var secondNumber = 0.0 //ตัวเลขสอง
    private var result = 0.0 //เก็บผลลัพธ์ในการคำนวณ
    private var isNewOperation = true //ใช้รีเซ็ตหน้าจอ เมื่อเริ่มการคำนวณใหม่

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //เชื่อม textview เข้ากับ layout
        display = findViewById(R.id.txtDisplay)

        //กลุ่มปุ่มตัวเลข 0-9
        val numberButtons = listOf(
            R.id.btn0,
            R.id.btn1,
            R.id.btn2,
            R.id.btn3,
            R.id.btn4,
            R.id.btn5,
            R.id.btn6,
            R.id.btn7,
            R.id.btn8,
            R.id.btn9
        )
        //กำหนด event ให้ปุ่มตัวเลขทุกปุ่ม
        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                numberClicked((it as Button).text.toString())
            }
        }

        //เชื่อมโยงปุ่มคำนวณ (เรียง + - * / %)
        findViewById<Button>(R.id.btnplus).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnminus).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnmultiply).setOnClickListener { onOperatorClick("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { onOperatorClick("/") }
        findViewById<Button>(R.id.btnMod).setOnClickListener { onOperatorClick("%") }

        //เชื่อมโยงปุ่มพิเศษ = AC DEL . +-
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDel).setOnClickListener { backSpace() }
        findViewById<Button>(R.id.btnDot).setOnClickListener { addDot() }
        findViewById<Button>(R.id.btnSign).setOnClickListener { toggleSign() }
    }
    //เมื่อกดปุ่มตัวเลข
    private fun numberClicked(number: String) {
        if (isNewOperation) {
            display.text = ""
            isNewOperation = false
        }
        current += number
        display.text = current
    }
    //เมื่อกดปุ่มเครื่องหมาย
    private fun onOperatorClick(op: String) {
        if (current.isNotEmpty()) {
            firstNumber = current.toDouble()
            operator = op
            current = ""
        }
    }
    //เมื่อกดปุ่มคำนวณ =
    private fun calculateResult() {
        if (current.isNotEmpty()) {
            secondNumber = current.toDouble()
            result = when (operator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
                "%" -> if (secondNumber != 0.0) firstNumber % secondNumber else Double.NaN
                else -> 0.0

            }
            //ถ้าหารด้วย 0
            display.text = if (result.isNaN()) "Cannot divide by zero" else result.toString()
            current = result.toString()
            isNewOperation = true

        }
    }
    //ฟังก์ชันของปุ่ม AC (all clear )
    private fun clearAll() {
        current = ""
        operator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        result = 0.0
        display.text = "0"
        isNewOperation = true

    }
    //ฟังก์ชั่นเพิ่ม dot
    private fun addDot() {
        if (!current.contains(".")){
            if (current.isEmpty()) current = "0" //ถ้ายังไม่มีเลข ให้ขึ้นต้นด้วย 0
            current += "."
            display.text = current
        }
    }

    //ฟังก์ชันของปุ่ม toggle sign สลับค่า
    private fun toggleSign() {
        if (current.isNotEmpty() && current != "0") {
            current = if (current.startsWith("-")) {
                current.substring(1)
            } else {
                "-$current"
            }
            display.text = current
        }
    }

    //ฟังก์ชันปุ่มลบทีละตัว
    private fun backSpace() {
        //ถ้าจอกำลังแสดงผลลัพธ์อยู่ จะจะไม่ให้ลบทีละตัว
        if (isNewOperation) return

        if (current.isNotEmpty()) {
            current = current.dropLast(1) //ตัดตัวสุดท้ายออก 1 ตัว

            if (current == "-" || current.isEmpty()) {
                current = ""
                display.text = "0"
            } else {
                display.text = current
            }
        }
    }


}
