package com.example.kotlinassessmentapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.kotlinassessmentapp.R

@Composable
fun rememberGoogleFontFamily(fontName: String): FontFamily {
    val context = LocalContext.current
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val googleFont = GoogleFont(fontName)
    return remember {
        FontFamily(Font(googleFont = googleFont, fontProvider = provider))
    }
}

@Composable
fun AppTypography(): Typography {
    val poppinsFontFamily = rememberGoogleFontFamily("Poppins") // Use your desired Google Font here

    return Typography(
        bodyLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            color = Color.White
        ),
        labelSmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}
