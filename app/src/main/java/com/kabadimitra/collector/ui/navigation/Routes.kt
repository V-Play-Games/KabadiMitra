package com.kabadimitra.collector.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Priceboard : Screen("priceboard")
    data object LotCapture : Screen("lot_capture")
    data object LotEstimate : Screen("lot_estimate/{lotId}") {
        fun createRoute(lotId: String) = "lot_estimate/$lotId"
    }
    data object Buyers : Screen("buyers/{lotId}") {
        fun createRoute(lotId: String) = "buyers/$lotId"
    }
    data object Handover : Screen("handover/{lotId}") {
        fun createRoute(lotId: String) = "handover/$lotId"
    }
    data object Receipt : Screen("receipt/{lotId}") {
        fun createRoute(lotId: String) = "receipt/$lotId"
    }
    data object Hisaab : Screen("hisaab")
    data object Safety : Screen("safety")
    data object Settings : Screen("settings")
}
