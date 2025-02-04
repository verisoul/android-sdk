package ai.verisoul.sampleapp.ui.main

import ai.verisoul.sampleapp.R
import ai.verisoul.sampleapp.ui.theme.VerisoulSampleAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerisoulExample(
    modifier: Modifier = Modifier,
    sessionId: String,
    callback: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Verisoul Logo",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Native Android Sample App",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = sessionId,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = callback
            ) {
                Text(text = "Get SessionId")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VerisoulSampleAppTheme {
        VerisoulExample(
            sessionId = "Sample Session ID",
            callback = {}
        )
    }
}
