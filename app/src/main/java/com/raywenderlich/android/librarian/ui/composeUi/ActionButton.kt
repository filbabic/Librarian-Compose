package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.animation.animate
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
@Preview
fun ActionButton(
  text: String = "Librarian",
  isEnabled: Boolean = true,
  enabledColor: Color = MaterialTheme.colors.primary,
  disabledTextColor: Color = Color.Gray,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {}
) {
  val backgroundColor =
    animate(target = if (isEnabled) enabledColor else Color.LightGray)

  TextButton(
    shape = RoundedCornerShape(16.dp),
    enabled = isEnabled,
    backgroundColor = backgroundColor,
    disabledContentColor = disabledTextColor,
    contentColor = MaterialTheme.colors.onSecondary,
    modifier = modifier
      .padding(16.dp),
    content = { Text(text = text) },
    onClick = onClick
  )
}