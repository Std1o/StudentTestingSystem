package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.Constants
import student.testing.system.common.showSnackbar
import student.testing.system.domain.states.RequestState
import student.testing.system.presentation.viewmodels.CoursesViewModel

@OptIn(NotScreenState::class)
@Composable
fun CoursesScreen() {
    val viewModel = hiltViewModel<CoursesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    Surface {
        when(val courses = uiState.courses) {
            is RequestState.Empty -> {}
            is RequestState.Error -> {}
            RequestState.Loading -> {}
            RequestState.NoState -> {}
            is RequestState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    courses.data.forEach{ course ->
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 16.dp)
                                    .height(150.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                AsyncImage(
                                    model = "${Constants.BASE_URL}images/${course.img}",
                                    contentDescription = "Translated description of what the image contains",
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.FillWidth
                                )
                                Text(
                                    text = course.name,
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    style = TextStyle(shadow = Shadow(Color.Black , Offset(3.0f, 4.95f), 1.0f))
                                )
                            }
                        }
                    }
                }
            }
            is RequestState.ValidationError -> {}
        }
    }
}