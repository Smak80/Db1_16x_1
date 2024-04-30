package ru.smak.db1_16x_1

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.db1_16x_1.room.StdGroup
import ru.smak.db1_16x_1.room.Student
import ru.smak.db1_16x_1.ui.theme.Db1_16x_1Theme

class MainActivity : ComponentActivity() {

    val mvm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Db1_16x_1Theme {
                // A surface container using the 'background' color from the theme
                val groups by mvm.groupsFlow.collectAsState(initial = listOf())
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUI(
                        groups,
                        mvm.newStud,
                        mvm.students,
                        Modifier.fillMaxSize(),
                        mvm.selectedGroup != null,
                        { mvm.newStud = it },
                        mvm::addStudent,
                        mvm::selectGroup
                    )
                }
            }
        }
    }
}

@Composable
fun MainUI(
    groups: List<StdGroup>,
    newStud: String,
    students: List<Student>,
    modifier: Modifier = Modifier,
    canAdd: Boolean = false,
    onNewStudChange: (String) -> Unit = {},
    onAddStudent: () -> Unit = {},
    onSelectGroup: (StdGroup) -> Unit = {}
) {
    Scaffold(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(it)) {
            LazyRow {
                items(groups) {
                    GroupInfo(it) { onSelectGroup(it) }
                }
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newStud,
                    onValueChange = onNewStudChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp, 0.dp)
                )
                Button(
                    onClick = onAddStudent,
                    modifier = Modifier.weight(0.3f),
                    enabled = canAdd,
                ) {
                    Icon(painter= painterResource(id = R.drawable.baseline_add_circle_outline_24),
                        contentDescription = null)
                }
            }
            LazyColumn {
                items(students) {
                    StudentInfo(it)
                }
            }
        }
    }
}

@Composable
fun GroupInfo(
    group: StdGroup,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
) {
    Card(
        modifier = modifier,
    ) {
        Column(modifier = Modifier
            .padding(8.dp)
            .clickable {
                onSelect()
            }) {
            Text(text = group.groupName)
            Text(text = group.direction)
        }
    }
}

@Composable
fun StudentInfo(
    student: Student,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = student.fullName)
        }
    }
}


@Preview
@Composable
fun GroupInfoPreview() {
    GroupInfo(group = StdGroup(1, "09-16x", "Информационные системы и технологии"))
}