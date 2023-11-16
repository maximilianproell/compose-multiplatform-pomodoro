package com.compose.multiplatform.pomodoro.ui.screens.statistics

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import at.maximilianproell.multiplatformchart.barchart.BarChart
import at.maximilianproell.multiplatformchart.barchart.model.BarChartEntry
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object StatisticsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel: StatisticsScreenModel = rememberScreenModel {
            StatisticsScreenModel()
        }

        val navigator = LocalNavigator.currentOrThrow
        val screenState by screenModel.state.collectAsState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Statistics")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.pop()
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddingValues ->
            // TODO: Just for testing
            BarChart(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                entries = listOf(
                    BarChartEntry("MO", 25f),
                    BarChartEntry("DI", 50f),
                    BarChartEntry("MI", 30f),
                    BarChartEntry("DO", 60f),
                ),
                maxYValue = 60f,
            )
            /*LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(screenState.workPackages) {
                    Row {
                        Text(it.id.toString())
                        Text(it.endDate.toString())
                        Text(it.minutes.toString())
                    }
                }
            }*/
        }
    }
}