package com.kabadimitra.collector.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kabadimitra.collector.core.audio.AudioClipPlayer
import com.kabadimitra.collector.core.designsystem.components.KmBottomNavBar
import com.kabadimitra.collector.core.designsystem.components.KmBottomTab
import com.kabadimitra.collector.data.di.AppContainer
import com.kabadimitra.collector.ui.buyers.BuyersScreen
import com.kabadimitra.collector.ui.handover.HandoverScreen
import com.kabadimitra.collector.ui.hisaab.HisaabScreen
import com.kabadimitra.collector.ui.home.HomeScreen
import com.kabadimitra.collector.ui.lot.capture.LotCaptureScreen
import com.kabadimitra.collector.ui.lot.estimate.LotEstimateScreen
import com.kabadimitra.collector.ui.onboarding.OnboardingScreen
import com.kabadimitra.collector.ui.priceboard.PriceboardScreen
import com.kabadimitra.collector.ui.receipt.ReceiptScreen
import com.kabadimitra.collector.ui.safety.SurakshaScreen
import com.kabadimitra.collector.ui.settings.SettingsScreen

@Composable
fun KmNavHost(
    container: AppContainer,
    audioPlayer: AudioClipPlayer,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tabs belonging to the persistent bottom bar
    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Priceboard.route,
        Screen.Hisaab.route,
        Screen.Safety.route
    )
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    val currentTab = when (currentRoute) {
        Screen.Priceboard.route -> KmBottomTab.BHAV
        Screen.Hisaab.route -> KmBottomTab.HISAAB
        Screen.Safety.route -> KmBottomTab.SURAKSHA
        else -> KmBottomTab.HOME
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar) {
                KmBottomNavBar(
                    selectedTab = currentTab,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 2. Home / Bhav Board
            composable(Screen.Home.route) {
                HomeScreen(
                    ratesFlow = container.database.priceDao().observeAllPrices(),
                    onStartLotCapture = {
                        navController.navigate(Screen.LotCapture.route)
                    },
                    onViewAllRates = {
                        navController.navigate(Screen.Priceboard.route)
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 3. Priceboard / Rates Tab
            composable(Screen.Priceboard.route) {
                PriceboardScreen(
                    ratesFlow = container.database.priceDao().observeAllPrices(),
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 4. Lot Capture (Pushed Step 1)
            composable(Screen.LotCapture.route) {
                LotCaptureScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onProceedToEstimate = { lotId, weight, material ->
                        navController.navigate(Screen.LotEstimate.createRoute(lotId))
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 5. Lot Estimate (Pushed Step 2)
            composable(Screen.LotEstimate.route) { backStackEntry ->
                val lotId = backStackEntry.arguments?.getString("lotId") ?: "KM-2026-0417"
                LotEstimateScreen(
                    lotId = lotId,
                    onNavigateBack = { navController.popBackStack() },
                    onProceedToBuyers = {
                        navController.navigate(Screen.Buyers.createRoute(lotId))
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 6. Buyers (Sahi Kharidar)
            composable(Screen.Buyers.route) { backStackEntry ->
                val lotId = backStackEntry.arguments?.getString("lotId") ?: "KM-2026-0417"
                BuyersScreen(
                    lotId = lotId,
                    recyclersFlow = container.database.recyclerDao().observeAllRecyclers(),
                    onNavigateBack = { navController.popBackStack() },
                    onRecyclerSelected = { recyclerId ->
                        navController.navigate(Screen.Handover.createRoute(lotId))
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 7. Handover (QR + OTP)
            composable(Screen.Handover.route) { backStackEntry ->
                val lotId = backStackEntry.arguments?.getString("lotId") ?: "KM-2026-0417"
                HandoverScreen(
                    lotId = lotId,
                    onNavigateBack = { navController.popBackStack() },
                    onCompleteHandover = {
                        navController.navigate(Screen.Receipt.createRoute(lotId)) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 8. Verified Receipt
            composable(Screen.Receipt.route) { backStackEntry ->
                val lotId = backStackEntry.arguments?.getString("lotId") ?: "KM-2026-0417"
                ReceiptScreen(
                    lotId = lotId,
                    onNavigateToHisaab = {
                        navController.navigate(Screen.Hisaab.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 9. Hisaab Ledger Tab
            composable(Screen.Hisaab.route) {
                HisaabScreen(
                    transactionsFlow = container.database.transactionDao().observeAllTransactions(),
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 10. Suraksha Safety Tab
            composable(Screen.Safety.route) {
                SurakshaScreen(
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 11. Settings
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }
        }
    }
}
