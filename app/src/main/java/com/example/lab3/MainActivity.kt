package com.example.lab3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.appcompat.widget.Toolbar
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var database: AppDatabase
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "game_of_thrones.db"
        ).build()

    }
}
