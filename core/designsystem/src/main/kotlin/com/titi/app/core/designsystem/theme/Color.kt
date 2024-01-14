package com.titi.app.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class TdsColorsPalette(
    val d1: Color = Color.Unspecified,
    val d2: Color = Color.Unspecified,
    val d3: Color = Color.Unspecified,
    val d4: Color = Color.Unspecified,
    val d5: Color = Color.Unspecified,
    val d6: Color = Color.Unspecified,
    val d7: Color = Color.Unspecified,
    val d8: Color = Color.Unspecified,
    val d9: Color = Color.Unspecified,
    val d10: Color = Color.Unspecified,
    val d11: Color = Color.Unspecified,
    val d12: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val backgroundColor: Color = Color.Unspecified,
    val redColor: Color = Color.Unspecified,
    val blueColor: Color = Color.Unspecified,
    val dividerColor: Color = Color.Unspecified,
    val lightGrayColor: Color = Color.Unspecified,
    val whiteColor: Color = Color.Unspecified,
    val blackColor: Color = Color.Unspecified,
)

val TdsLightColorsPalette =
    TdsColorsPalette(
        d1 = Color(0xFF8299E3),
        d2 = Color(0xFF9AC0DA),
        d3 = Color(0xFF97D2C2),
        d4 = Color(0xFFA6DE9A),
        d5 = Color(0xFFFCE672),
        d6 = Color(0xFFFFC568),
        d7 = Color(0xFFFEA97E),
        d8 = Color(0xFFFF9CA1),
        d9 = Color(0xFFD19AD0),
        d10 = Color(0xFFD19AD0),
        d11 = Color(0xFFAF8CD8),
        d12 = Color(0xFF928AEA),
        textColor = Color(0xFF000000),
        backgroundColor = Color(0xFFFFFFFF),
        redColor = Color(0xFFFF453A),
        blueColor = Color(0xFF0A84FF),
        dividerColor = Color(0x4D000000),
        lightGrayColor = Color(0xFF555555),
        whiteColor = Color(0xFFFFFFFF),
        blackColor = Color(0xFF000000),
    )

val TdsDarkColorsPalette =
    TdsColorsPalette(
        d1 = Color(0xFF87A6F8),
        d2 = Color(0xFFA7D7F9),
        d3 = Color(0xFFA7FAE8),
        d4 = Color(0xFFA4FBB9),
        d5 = Color(0xFFF8FC95),
        d6 = Color(0xFFEDC38D),
        d7 = Color(0xFFEDA479),
        d8 = Color(0xFFEB827E),
        d9 = Color(0xFFF0ABAD),
        d10 = Color(0xFFCF9AD1),
        d11 = Color(0xFF896FC7),
        d12 = Color(0xFF6379EB),
        textColor = Color(0xFFFFFFFF),
        backgroundColor = Color(0xFF000000),
        redColor = Color(0xFFFF453A),
        blueColor = Color(0xFF0A84FF),
        dividerColor = Color(0x99000000),
        lightGrayColor = Color(0xFF555555),
        whiteColor = Color(0xFFFFFFFF),
        blackColor = Color(0xFF000000),
    )

enum class TdsColor {
    D1,
    D2,
    D3,
    D4,
    D5,
    D6,
    D7,
    D8,
    D9,
    D10,
    D11,
    D12,

    TEXT,
    BACKGROUND,
    RED,
    BLUE,
    DIVIDER,

    LIGHT_GRAY,
    WHITE,
    BLACK,
    ;

    @Composable
    fun getColor() = when (this) {
        D1 -> TiTiTheme.colors.d1
        D2 -> TiTiTheme.colors.d2
        D3 -> TiTiTheme.colors.d3
        D4 -> TiTiTheme.colors.d4
        D5 -> TiTiTheme.colors.d5
        D6 -> TiTiTheme.colors.d6
        D7 -> TiTiTheme.colors.d7
        D8 -> TiTiTheme.colors.d8
        D9 -> TiTiTheme.colors.d9
        D10 -> TiTiTheme.colors.d10
        D11 -> TiTiTheme.colors.d11
        D12 -> TiTiTheme.colors.d12

        TEXT -> TiTiTheme.colors.textColor
        BACKGROUND -> TiTiTheme.colors.backgroundColor
        RED -> TiTiTheme.colors.redColor
        BLUE -> TiTiTheme.colors.blueColor
        DIVIDER -> TiTiTheme.colors.dividerColor

        LIGHT_GRAY -> TiTiTheme.colors.lightGrayColor
        WHITE -> TiTiTheme.colors.whiteColor
        BLACK -> TiTiTheme.colors.blackColor
    }
}
