package student.testing.system.presentation.ui.screens.questionCreation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.common.iTems
import student.testing.system.domain.models.Answer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnswersList(answers: List<Answer>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)) {
        iTems(answers, key = { it }) { answer ->
            val shape = RoundedCornerShape(5.dp)
            Card(
                elevation = 10.dp,
                shape = shape,
                modifier = Modifier
                    .animateItem()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .combinedClickable(
                            onClick = { },
                            onLongClick = { },
                        )
                ) {
                    var checked by rememberSaveable { mutableStateOf(false) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .toggleable(
                                value = checked,
                                role = Role.Checkbox,
                                onValueChange = {
                                    checked = !checked
                                    answer.isRight = checked
                                }
                            )
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isRight ->
                                checked = !checked
                                answer.isRight = isRight
                            })
                        Text(
                            text = answer.answer, modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f),
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}