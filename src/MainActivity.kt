package com.horvathkaroly.feedthecat

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var steakImageView: ImageView
    private lateinit var catImageView: ImageView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var scoreTextView: TextView
    private lateinit var livesTextView: TextView
    private var lastX: Float = 0f
    private var isFirstAnimation = true
    private var score = 0
    private var lives = 3

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        steakImageView = findViewById(R.id.steakImageView)
        catImageView = findViewById(R.id.catImageView)
        constraintLayout = findViewById(R.id.constraintLayout)
        scoreTextView = findViewById(R.id.scoreTextView)
        livesTextView = findViewById(R.id.livesTextView)

        catImageView.setOnTouchListener { _, event ->
            handleTouch(event)
        }
        updateScoreTextView()
        updateLivesTextView()
    }

    private fun handleTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastX
                moveCatImage(deltaX)
                lastX = event.x
            }
        }
        return true
    }

    private fun moveCatImage(deltaX: Float) {
        catImageView.translationX = catImageView.translationX + deltaX
    }

    private fun placeSteakImageRandomly() {
        val screenWidth = constraintLayout.width
        val randomX = Random.nextInt(0, screenWidth - steakImageView.width)
        val randomY = 0

        val params = steakImageView.layoutParams as ConstraintLayout.LayoutParams
        params.leftMargin = randomX
        params.topMargin = randomY
        steakImageView.layoutParams = params
    }

    private fun startMovingAnimation() {
        val animator = ObjectAnimator.ofFloat(
            steakImageView,
            "translationY",
            0f,
            constraintLayout.height.toFloat()
        )
        animator.duration = 2000
        animator.interpolator = AccelerateInterpolator()

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                isCatCatchingSteak()

                placeSteakImageRandomly()
                startMovingAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        animator.start()
    }

    private fun isCatCatchingSteak(): Boolean {
        val catRect = Rect()
        catImageView.getGlobalVisibleRect(catRect)

        val steakRect = Rect()
        steakImageView.getGlobalVisibleRect(steakRect)

        val isCatching = catRect.intersect(steakRect)

        if (isCatching) {
            score++
            updateScoreTextView()
        } else {
            lives--
            updateLivesTextView()

            if (lives == 0) {
                gameOver()
            }
        }

        return isCatching
    }

    @SuppressLint("SetTextI18n")
    private fun updateLivesTextView() {
        livesTextView.text = "Lives: $lives"
    }

    private fun gameOver() {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("SCORE", score)
        startActivity(intent)
        finish()
    }


    @SuppressLint("SetTextI18n")
    private fun updateScoreTextView() {
        scoreTextView.text = "Score: $score"
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && isFirstAnimation) {
            placeSteakImageRandomly()
            startMovingAnimation()
            isFirstAnimation = false
        }
    }
}

