package com.compose.multiplatform.pomodoro.ui.screens.statistics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.ui.components.BackIcon
import com.compose.multiplatform.pomodoro.ui.components.chart.WeeklyBarchart
import dev.icerock.moko.resources.compose.stringResource

object StatisticsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
                        Text(stringResource(MR.strings.bar_chart_title_hours_worked))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.pop()
                        }) {
                            BackIcon()
                        }
                    }
                )
            }
        ) { paddingValues ->
            val numberOfPages = screenState.numberOfPages
            val pagerState = rememberPagerState(pageCount = { numberOfPages })
            LaunchedEffect(numberOfPages) {
                pagerState.scrollToPage(numberOfPages - 1)
            }

            HorizontalPager(
                state = pagerState,
                contentPadding = paddingValues,
                beyondBoundsPageCount = 3,
            ) { index ->
                WeeklyBarchart(
                    modifier = Modifier.padding(16.dp),
                    weeksFromCurrentWeek = numberOfPages - 1 - index
                )
            }
        }
    }
}