package student.testing.system.presentation.ui.screens.tests

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.common.iTems
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.Test
import student.testing.system.presentation.ui.activity.ui.theme.Purple700
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestsList(
    isLoading: Boolean,
    hidden: Boolean,
    tests: LoadableData<List<Test>>,
    onClick: (Test) -> Unit,
    onLongClick: (Test) -> Unit
) {
    if (hidden) return
    val data = (tests as? LoadableData.Success)?.data
    val mockTests = listOf(Test(id = 0), Test(id = 1), Test(id = 2))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(data ?: mockTests, key = { it }) { test ->
            val shape = RoundedCornerShape(5.dp)
            Card(
                elevation = 10.dp,
                shape = shape,
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .combinedClickable(
                            onClick = { onClick(test) },
                            onLongClick = { onLongClick(test) },
                        )
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                        Text(
                            text = test.name,
                            modifier = Modifier
                                .widthIn(min = 60.dp)
                                .clip(CircleShape)
                                .placeholder(isLoading, Shimmer()),
                            fontSize = 14.sp,
                            color = Purple700
                        )
                        Text(
                            text = test.creationTime,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(CircleShape)
                                .widthIn(min = 100.dp)
                                .placeholder(isLoading, Shimmer()),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}