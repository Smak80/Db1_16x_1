package ru.smak.db1_16x_1

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.db1_16x_1.database.StdGroup
import ru.smak.db1_16x_1.ui.theme.Db1_16x_1Theme

class MainActivity : ComponentActivity() {

    val mvm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Db1_16x_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUI(mvm.groups, Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun MainUI(
    groups: List<StdGroup>,
    modifier: Modifier = Modifier
){
    Scaffold(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(it)) {
            LazyRow{
                items(groups){
                    GroupInfo(it)
                }
            }
        }
    }
}

@Composable
fun GroupInfo(
    group: StdGroup,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(8.dp).clickable {

        }) {
            Text(text = group.groupName)
            Text(text = group.direction)
        }
    }
}

@Preview
@Composable
fun GroupInfoPreview(){
    GroupInfo(group = StdGroup(1, "09-16x", "Информационные системы и технологии"))
}