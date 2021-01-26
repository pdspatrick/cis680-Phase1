package com.patrick.mortgagecalc

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun submitDataFromMain(view: View){
        var loanData = Mortgage()
        //loanData.interestRate = inter
        loanData.initialBalance = R.id.loanAmount.toFloat()
        loanData.setDownPayment(R.id.downPayment.toDouble())
        //loanData.setExtraPayment(R.id.extraMonthlyPayment.toFloat(), R.id.addExtraPayments.)
        loanData.setInitLength(R.id.termOfLoan)
        var resultantTable = loanData.amTable()

    }
    fun main(){
        val weeee = interestRate
        print(weeee)
    }
}
