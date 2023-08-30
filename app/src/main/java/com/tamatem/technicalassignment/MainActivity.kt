package com.tamatem.technicalassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tamatem.technicalassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /*
    viewbinding is a feature that makes writing code that interacts with the views easier
    and I used it instead of findViewById because it makes less boilerplate code and it's generate a code
    that more readable as you no longer need to write repetitive "findViewById" calls
     */
    private lateinit var viewBinding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setUpListeners()
    }

    private fun setUpListeners(){
        viewBinding.apply {
            showWebPageBtn.setOnClickListener {
                /*
                I used starter pattern to avoid runtime errors and that's happens when adding extra data
                 */
                startActivity(WebPageActivity.getStartIntent(this@MainActivity))
            }
        }
    }
}