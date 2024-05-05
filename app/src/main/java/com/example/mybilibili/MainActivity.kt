package com.example.mybilibili

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybilibili.ui.theme.MyBilibiliTheme
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBilibiliTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BilibiliApp(
                        topBar = { TopBar() },
                        bottomBar = { NavBar() },
                        content = {
                            NamesBar(paddingValues = it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BilibiliApp(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
    modifier: Modifier = Modifier,

    ) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        content = content,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Row(horizontalArrangement = Arrangement.Center) {
                var searchText by remember {
                    mutableStateOf("")
                }

                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    Modifier
                        .padding(start = 24.dp)
                        .weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp)
                ) {
                    if (searchText.isEmpty()) {
                        Text(text = "搜搜看？", color = Color.Gray, fontSize = 16.sp)

                    }
                    it()

                }
            }

        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Face, contentDescription = null)
            }
        },
        actions = {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Email, contentDescription = null)
            }
        }

    )
}

@Composable
fun NavBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavItem(icon = R.drawable.home, Color(0xffff3399), "首页")
        NavItem(icon = R.drawable.dontai, Color(0xffb4b4b4), "动态")
        NavItem(icon = R.drawable.add, Color(0xffb4b4b4))
        NavItem(icon = R.drawable.buy, Color(0xffb4b4b4), "购物")
        NavItem(icon = R.drawable.tv, Color(0xffb4b4b4), "我的")
    }
}

@Composable
private fun RowScope.NavItem(
    @DrawableRes icon: Int,
    tint: Color,
    name: String = "",
) {
    Button(
        onClick = { /*TODO*/ },
        Modifier
            .weight(1f),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(0.dp)
        ) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "图标",
                modifier = Modifier
                    .size(25.dp)
                    .weight(1f)
                    .fillMaxSize(),
                tint = tint
            )
            if (!name.isEmpty()) {
                Text(text = name, color = tint, fontSize = 14.sp, modifier = Modifier.width(60.dp))
            }

        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NamesBar(paddingValues: PaddingValues) {
    val names = listOf("推荐", "热门")
    var selected by remember {
        mutableStateOf(0)
    }
    LazyRow(
        Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        contentPadding = PaddingValues(12.dp, 0.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(names) { index, name ->
            Text(
                text = name,
                color = if (selected == index) Color(0xffff00cc) else Color(0xffb4b4b4)
            )

        }
    }
    val state = rememberPagerState { 2 }
    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxHeight(),
    ) { page ->

        Column(
            modifier = Modifier
                .padding(0.dp)
//                .background(Color.Blue)
                .size(500.dp, 600.dp),
//                .aspectRatio(1f)
//            contentAlignment = Alignment.Center
        ) {
            Carousel()
            VedioFeed()
        }
        selected = page
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    // 轮播图

) {
    val images =
        listOf(R.drawable.lun1, R.drawable.lun2, R.drawable.lun3, R.drawable.lun4, R.drawable.lun5)
    val state = rememberPagerState { 5 }
    HorizontalPager(
        state = state,
//        modifier = Modifier.fillMaxHeight()
        Modifier.padding(20.dp)
    ) { page ->
        Image(painter = painterResource(id = images[page]), contentDescription = null)
    }
}

@Composable
fun VedioFeed() {
    val list = (1..100).map { it.toString() }
    val images =
        listOf(R.drawable.lun1, R.drawable.lun2, R.drawable.lun3, R.drawable.lun4, R.drawable.lun5)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.size(500.dp, 500.dp),

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(list.size) { index ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(id = images[(list[index]).toInt() % 5]),
                        contentDescription = null
                    )
                    Text(text = "视频描述。。。。。。。")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyBilibiliTheme {
        BilibiliApp(
            topBar = { TopBar() },
            bottomBar = { NavBar() },
            content = {
                NamesBar(paddingValues = it)
            }
        )
    }
}