package com.tamatem.technicalassignment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tamatem.technicalassignment.databinding.ActivityWebPageBinding
import com.tamatem.technicalassignment.viewModel.AppViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WebPageActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityWebPageBinding
    private val viewModel:AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWebPageBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_PROGRESS);

        setContentView(viewBinding.root)


        setWebViewUrl()
        setUpObserver()

    }

    private fun setWebViewUrl(){
        /*
        I used the mvvm (model view viewModel) because it's  have some benefits such as
        1. code separation: it keeps the UI and data logic separate and that makes the code cleaner
        and there are many more benefits
         */
        viewModel.setUrl("https://tamatemplus.com")
    }


    private fun setUpObserver(){
        lifecycleScope.launch {
            /*
            I used mutable state flow to notify the observer whenever the state changes an
            and it makes the UI  automatically update based on data changes.
             */
            viewModel.uiState.collectLatest {
                when(it){
                    is AppViewModel.AppUiState.UrlSetSuccessfully ->{
                        setUpWebPage(it.url)
                    }

                    else -> {}
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebPage(url:String){
        viewBinding.apply {
            webView.apply {
                /*
                the WebView will be able to execute JavaScript code contained in the loaded web page.
                 This can include animations, form validation, and without it the webpage will not open
                 */
                settings.javaScriptEnabled = true
                /*
                if I remove this line the webpage will not open because of some javascript errors
                and that's appears on  logs

                DOM Storage is a web browser feature that allows web pages
                to store data locally in the user's browser.
                 */
                settings.domStorageEnabled = true

                /*
                When the user clicks a link from a web page in WebView ,
                the default behavior is for Android to launch an application that handles URLs.
                 Usually, the default web browser opens and loads the destination URL.
                 webViewClient can override this behavior for the WebView, so links open within app WebView.
                 */
                webViewClient = WebViewClient()
                /*
                WebChromeClient is used to handle a JavaScript events in Android App
                 which are produced by WebView. example of that onProgressChanged
                 */
                webChromeClient = object :WebChromeClient(){
                    /*
                    this override function is to tell the app of the current progress of a loading page
                     */
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        /*
                        I added a progress bar that will be disappear when the progress is equal to 100
                         */
                        if (newProgress == 100)
                            progressBar.visibility = View.GONE
                    }
                }
                loadUrl(url)


            }


            backButton.setOnClickListener {
                /*
                check if the webpage has a back history and if it is then we can go back
                otherwise the app will do nothing
                 */
                if (webView.canGoBack()) {
                    webView.goBack() // Navigate back in the WebView's history
                }
            }

            forwardButton.setOnClickListener {
                /*
                check if the web page has a forward history and if it is then we can go forward
                otherwise the app will not do anything
                 */
                if (webView.canGoForward()) {
                    webView.goForward() // Navigate forward in the WebView's history
                }
            }

            refreshButton.setOnClickListener {
                //to reload the webpage
                webView.reload()
            }

            closeButton.setOnClickListener {
                //finishing the web page activity
                finish()
            }
        }
    }


    override fun onBackPressed() {
        //I removed the super.onBackPressed() to disable the back button functionality
    }



    companion object{
        /*
        this function is a starter pattern I used it instead of using
        startActivity(Intent(context,secondActivity::class.java)
        because sometimes this line of code will make a runtime error and that's maybe happen if
        I want to send some data. so the starter pattern solved this issue
         */
        fun getStartIntent(context:Context):Intent{
            return Intent(context,WebPageActivity::class.java)
        }
    }
}