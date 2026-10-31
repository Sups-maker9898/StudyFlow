package com.jeiu.studyflow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jeiu.studyflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Keep content clear of the status and navigation bars (edge-to-edge on new Android versions).
        ViewCompat.setOnApplyWindowInsetsListener(bind.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController
        bind.bottomNav.setupWithNavController(navController)

        // Bottom navigation only on the two top-level screens.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val topLevel = destination.id == R.id.assignmentListFragment ||
                    destination.id == R.id.courseListFragment
            bind.bottomNav.visibility = if (topLevel) View.VISIBLE else View.GONE
        }
    }
}
