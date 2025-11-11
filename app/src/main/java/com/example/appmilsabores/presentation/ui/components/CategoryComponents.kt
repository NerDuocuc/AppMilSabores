package com.example.appmilsabores.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.presentation.ui.theme.CardBackgroundColor
import com.example.appmilsabores.presentation.ui.theme.PrimaryPurple

@Composable
fun CategoryCard(category: CategoryInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .aspectRatio(1f)
            .border(2.dp, PrimaryPurple, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // If category provides a representative image, show it taking most of the card.
            if (category.imageRes != null) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.name,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay with gradient-like scrim could be added; for simplicity show title at top.
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(category.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                }
            } else {
                // Fallback: show name and icon like before
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(category.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Icon(category.icon, contentDescription = category.name, tint = PrimaryPurple)
                    }
                    Spacer(Modifier.weight(1f))
                    Column {
                        category.sampleProducts.forEach { productName ->
                            Text("• $productName", color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
