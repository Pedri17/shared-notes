package com.pproject.sharednotes.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.presentation.screens.home.components.FolderHorizontalGrid
import com.pproject.sharednotes.presentation.screens.home.components.HomeHeader

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
) {
    Surface {
        Column {
            HomeHeader()
            LazyColumn {
                items(
                    items = homeViewModel.getFolders(),
                ) { folderID ->
                    FolderHorizontalGrid(
                        folder = homeViewModel.getFolderFromID(folderID),
                        navController = navController,
                        onGetNote = { homeViewModel.getNoteFromID(it) },
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }
}

