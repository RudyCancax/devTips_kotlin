package com.example.devtips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupPositionProvider
import com.example.compose.DevTipsTheme
import com.example.compose.primaryContainerDark
import com.example.compose.primaryLightMediumContrast
import com.example.devtips.data.TipsRepository
import com.example.devtips.model.Tip

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevTipsTheme {
                DevTipsApp();
            }
        }
    }
}

@Composable
fun DevTipsApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Spacer(modifier = Modifier.height(50.dp))
            TipsAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    ) { it ->
        TipsGrid(tipsList = TipsRepository.heroes, modifier = Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsAppBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.code),
                    contentDescription = null
                )
                Text(
                    text = "${TipsRepository.heroes.size} ${stringResource(R.string.app_name)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun TipsGrid(tipsList: List<Tip>, modifier: Modifier = Modifier) {
    var displayTipModal by remember {
        mutableStateOf(false)
    }
    var currentTip by remember {
        mutableStateOf<Tip?>(null)
    }
    var currentTipNumber by remember {
        mutableStateOf(0)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        itemsIndexed(tipsList) { index, tip ->
            TipItem(
                tip,
                index,
                modifier = Modifier
                    .padding(4.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = MaterialTheme.shapes.medium,
                        clip = true,
                        ambientColor = MaterialTheme.colorScheme.primaryContainer,
                        spotColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
            ) {
                displayTipModal = true;
                currentTip = tip;
                currentTipNumber = index + 1;
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))

    if (displayTipModal) {
        AlertDialog(onDismissRequest = {
            displayTipModal = false;
        },
            title = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tip # $currentTipNumber",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = stringResource(currentTip?.titleId ?: R.string.tip_title_1),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
           },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(currentTip?.iconId ?: R.string.tip_icon_1),
                        textAlign = TextAlign.Center,
                        fontSize = 100.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(currentTip?.descriptionId ?: R.string.tip_title_1),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = { displayTipModal = false }) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { displayTipModal = false }) {
                    Text(text = "Cancelar")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
            containerColor = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Composable
fun TipItem( tip: Tip, id: Int, modifier: Modifier = Modifier, onClick: () -> Unit,  ) {
    Card(modifier = modifier.clickable { onClick() }) {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = 80.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(4.dp)
                ) {
                    Text(text = "${id + 1}", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.surface)
                }
                Text(
                    text = stringResource(tip.iconId),
                    textAlign = TextAlign.Center,
                    fontSize = 50.sp,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DevTipsApp()
}