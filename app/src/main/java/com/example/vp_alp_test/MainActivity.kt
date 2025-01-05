package com.example.vp_alp_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.vp_alp_test.ui.theme.VP_ALP_TestTheme
import com.example.vp_alp_test.view.App


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VP_ALP_TestTheme {
                // TODO: add routers here
                App()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    VP_ALP_TestTheme {
        App()
    }
}