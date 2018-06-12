package com.rawee.app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private var currentFragment = R.id.navigation_books

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        loadFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { onBottomNavMenuSelected(it) }

    }

    private fun onBottomNavMenuSelected(it: MenuItem): Boolean {
        if (it.itemId == currentFragment)
            return false
        currentFragment = it.itemId
        when (it.itemId) {
            R.id.navigation_books -> {
                loadFragment(HomeFragment())
                return true
            }
            R.id.navigation_search -> {
                loadFragment(HomeFragment())
                return true
            }
            R.id.navigation_cart -> {
                loadFragment(HomeFragment())
                return true
            }
        }
        return false
    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        //transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
