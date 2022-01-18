package com.hti.Grad_Project.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hti.Grad_Project.Activities.ui.theme.ShimmerAnimationJetPackComposeTheme


@Preview
@Composable
fun Preview2() {
    ShimmerAnimationJetPackComposeTheme {
        ShimmerEnhancedGoogle()
    }
}

@Composable
fun ShimmerAnimateBookItem() {
    val shimmerColors = listOf(
        Color.LightGray.copy(0.9f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.9f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    ShimmerBookItem(brush)
}

@Composable
fun ShimmerBookItem(brush: Brush) {
    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),

        ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 0.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "",
                        modifier = Modifier
                            .background(brush)
                            .height(15.dp)
                            .fillMaxWidth(),
                        maxLines = 2,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(brush)
                            .height(9.dp)
                            .width(300.dp)

                    )

                }

            }

        }

    }


}


@Composable
fun ShimmerEnhancedGoogle() {
    val shimmerColors = listOf(
        Color.LightGray.copy(0.9f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.9f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    ShimmerEnhancedGoogle(brush)
}


@Composable
fun ShimmerEnhancedGoogle(brush: Brush) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column {
                Text(
                    text = "",
                    modifier = Modifier
                        .background(brush)
                        .height(15.dp)
                        .fillMaxWidth(),
                    maxLines = 2,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .background(brush)
                        .height(100.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(brush)
                            .height(20.dp)
                            .width(60.dp)
                    )

                    Text(
                        text = "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(brush)
                            .height(20.dp)
                            .width(100.dp)
                    )
                }
            }


        }

    }


}

