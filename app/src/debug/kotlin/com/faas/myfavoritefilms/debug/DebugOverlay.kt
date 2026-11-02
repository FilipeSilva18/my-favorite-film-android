package com.faas.myfavoritefilms.debug

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.faas.myfavoritefilms.R

@Composable
fun DebugOverlay(modifier: Modifier = Modifier) {
    SmallFloatingActionButton(
        onClick = { throw TestCrashException() },
        modifier = modifier,
    ) {
        Icon(Icons.Filled.Warning, contentDescription = stringResource(R.string.debug_force_crash))
    }
}

class TestCrashException : RuntimeException("Crashlytics test crash (debug only)")
