package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentRulerBinding
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import androidx.navigation.fragment.findNavController


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class RulerFragment : Fragment(R.layout.fragment_ruler) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surfaceView = view.findViewById<SurfaceView>(R.id.rulerSurfaceView)
        val holder = surfaceView.holder

        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                drawRuler(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                drawRuler(holder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })

        val backButton: Button = view.findViewById(R.id.button)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_rulerFragment_to_menuFragment) // 예: 이전 화면으로 돌아가기
        }
    }

    private fun drawRuler(holder: SurfaceHolder) {
        val canvas: Canvas = holder.lockCanvas()
        canvas.drawColor(Color.WHITE) // 배경색 설정

        val paint = Paint().apply {
            isAntiAlias = true
            textSize = 12 * resources.displayMetrics.scaledDensity
            strokeWidth = 3f
        }

        // 화면 밀도에 따른 1mm 간격 계산
        val mmToPx = resources.displayMetrics.ydpi / 25.4f
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        // 시작 좌표
        var yPosition = 0f
        var tickNumber = 0

        val initialTickLength = 2 * mmToPx // 2mm 길이
        val initialTickColor = Color.BLACK // 검은색
        val initialTickStrokeWidth = 3f // 기본 굵기

        paint.color = initialTickColor
        paint.strokeWidth = initialTickStrokeWidth

        // 첫 번째 눈금
        canvas.drawLine(0f, yPosition, initialTickLength, yPosition, paint)
        yPosition += mmToPx

        // 두 번째 눈금
        canvas.drawLine(0f, yPosition, initialTickLength, yPosition, paint)
        yPosition += mmToPx


        while (yPosition < screenHeight) {
            for (i in 0..9) {
                val tickLength: Float
                val tickColor: Int
                val tickStrokeWidth: Float

                when (i) {
                    0 -> {
                        tickLength = 3 * mmToPx // 첫 번째 눈금: 3mm
                        tickColor = Color.RED // 빨간색
                        tickStrokeWidth = 5f

                        val textMargin = 2 * mmToPx // 눈금과 텍스트 간 거리 2mm
                        paint.color = Color.BLACK // 숫자는 검은색
                        paint.strokeWidth = 1f // 텍스트 굵기 초기화

                        // 텍스트 크기 계산
                        val text = tickNumber.toString()
                        val textBounds = android.graphics.Rect()
                        paint.getTextBounds(text, 0, text.length, textBounds)
                        val textWidth = textBounds.width()
                        val textHeight = textBounds.height()

                        // 텍스트를 빨간 눈금 중심에 맞추기
                        val textX = tickLength + textMargin // X 좌표는 고정
                        val textY = yPosition + textHeight/2 // 텍스트 높이 중심 조정

                        // 텍스트를 회전해서 표시
                        canvas.save() // 현재 상태 저장
                        canvas.rotate(90f, textX, yPosition) // 90도 회전
                        canvas.drawText(
                            text,
                            textX - textWidth / 2f, // 중심 정렬
                            textY,
                            paint
                        )
                        canvas.restore() // 이전 상태 복원
                        tickNumber++


                    }
                    5 -> {
                        tickLength = 2.5f * mmToPx // 여섯 번째 눈금: 2.5mm
                        tickColor = Color.BLACK // 검은색
                        tickStrokeWidth = 3f
                    }
                    else -> {
                        tickLength = 2 * mmToPx // 나머지 눈금: 2mm
                        tickColor = Color.BLACK // 검은색
                        tickStrokeWidth = 3f
                    }


                }

                paint.color = tickColor
                paint.strokeWidth = tickStrokeWidth

                // 눈금 그리기
                canvas.drawLine(0f, yPosition, tickLength, yPosition, paint)

                // 다음 눈금으로 이동
                yPosition += mmToPx


            }
        }

        holder.unlockCanvasAndPost(canvas)
    }
}