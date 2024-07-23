@file:OptIn(ExperimentalFoundationApi::class)

package com.plcoding.composedraganddrop

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val boxCount = 5
        var dragBoxIndex by remember {
            mutableIntStateOf(0)
        }
        val colors = remember {
            (1..boxCount).map {
                Color(Random.nextLong()).copy(alpha = 1f)
            }
        }
        repeat(boxCount) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(colors[index])
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = { event ->
                            event
                                .mimeTypes()
                                .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                        },
                        target = remember {
                            object : DragAndDropTarget {
                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    val text = event.toAndroidDragEvent().clipData
                                        ?.getItemAt(0)?.text
                                    println("Drag data was $text")

                                    dragBoxIndex = index
                                    return true
                                }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                this@Column.AnimatedVisibility(
                    visible = index == dragBoxIndex,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Text(
                        text = "Drag me!",
                        fontSize = 40.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .dragAndDropSource(
//                                drawDragDecoration = {
//                                    drawRect(
//                                        color = Color.Red
//                                    )
//                                }
                            ) {
                                detectTapGestures(
                                    onLongPress = { offset ->
                                        startTransfer(
                                            transferData = DragAndDropTransferData(
                                                clipData = ClipData.newPlainText(
                                                    "text",
                                                    "Drag me!"
                                                )
                                            )
                                        )
                                    }
                                )
                            }
                    )
                }
            }
        }
    }
}