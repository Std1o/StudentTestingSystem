package student.testing.system.presentation.ui.screens.courses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import student.testing.system.common.Constants
import student.testing.system.common.Utils
import student.testing.system.common.iTems
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.CourseResponse
import student.testing.system.presentation.ui.components.Shimmer
import student.testing.system.presentation.ui.components.modifiers.placeholder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoursesList(
    isLoading: Boolean,
    hidden: Boolean,
    courses: LoadableData<List<CourseResponse>>,
    onClick: (CourseResponse) -> Unit,
    onLongClick: (CourseResponse) -> Unit
) {
    if (hidden) return
    val data = (courses as? LoadableData.Success)?.data
    val fakeCourses = listOf(CourseResponse(id = 0), CourseResponse(id = 1), CourseResponse(id = 2))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        iTems(data ?: fakeCourses, key = { it }) { course ->
            Box(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .placeholder(isLoading, Shimmer())
                    .combinedClickable(
                        onClick = { onClick(course) },
                        onLongClick = { if (Utils.isUserAnOwner(course)) onLongClick(course) else null },
                    )
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
                    style = TextStyle(
                        shadow = Shadow(
                            Color.Black,
                            Offset(3.0f, 4.95f),
                            1.0f
                        )
                    )
                )
            }
        }
    }
}