package com.dicoding.jetreward.ui.screen.home

import android.widget.SearchView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.jetreward.di.Injection
import com.dicoding.jetreward.model.OrderReward
import com.dicoding.jetreward.ui.ViewModelFactory
import com.dicoding.jetreward.ui.common.UiState
import com.dicoding.jetreward.ui.components.RewardItem


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllRewards()
            }
            is UiState.Success -> {

                Column {
                    SearchView(textState.value)
                    HomeContent(
                        orderReward = uiState.data,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                    )
                }


            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    orderReward: List<OrderReward>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {

        items(orderReward) { data ->
            RewardItem(
                image = data.reward.image,
                title = data.reward.title,
                requiredPoint = data.reward.requiredPoint,
                modifier = Modifier.clickable {
                    navigateToDetail(data.reward.id)
                }
            )
        }
    }
}
