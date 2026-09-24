package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandSurface

@Composable
fun WhatsAppFloatingButton(
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color(0xFF25D366),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(6.dp),
            shape = CircleShape,
            modifier = Modifier
                .size(56.dp)
                .testTag("floating_whatsapp_button")
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat on WhatsApp with Dharti Rajasthan Concierge",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    if (showDialog) {
        WhatsAppConciergeDialog(onDismiss = { showDialog = false })
    }
}

@Composable
fun WhatsAppConciergeDialog(onDismiss: () -> Unit) {
    var userMessage by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Header (WhatsApp Green + Royal Branding)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF075E54))
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.SupportAgent,
                                    contentDescription = null,
                                    tint = RoyalMaroon,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Dharti Rajasthan Support",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Artisan Concierge • Online",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFB8E6CB),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                // Body FAQs & Chat simulation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE5DDD5).copy(alpha = 0.3f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Khamma Ghani! How can our master artisans assist you today?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF333333),
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Frequent Questions:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val quickPrompts = listOf(
                        "How do I choose the correct Jutti size?",
                        "What is your 7-Day Size Exchange policy?",
                        "Can I get custom bridal embroidery?",
                        "Track my dispatched order"
                    )

                    quickPrompts.forEach { prompt ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, RoyalGold.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable {
                                    sentMessage = prompt
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.QuestionAnswer,
                                    contentDescription = null,
                                    tint = RoyalMaroon,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF222222)
                                    )
                                )
                            }
                        }
                    }

                    if (sentMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDCF8C6))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = sentMessage!!,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1B3D2F))
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, RoyalGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Our artisan assistant is connecting you on WhatsApp (+91 98290 XXXXX). We ensure 100% bite-free replacement guarantee within 7 days!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF222222),
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
