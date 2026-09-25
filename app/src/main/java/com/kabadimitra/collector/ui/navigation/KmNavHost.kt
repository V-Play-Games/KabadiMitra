package com.kabadimitra.collector.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kabadimitra.collector.core.audio.AudioClipPlayer
import com.kabadimitra.collector.core.designsystem.AmberAlert
import com.kabadimitra.collector.core.designsystem.AmberLight
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
import kotlinx.coroutines.launch

@Composable
fun KmNavHost(
    container: AppContainer,
    audioPlayer: AudioClipPlayer,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()
    val isOnline by container.networkMonitor.isOnline.collectAsState()

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
        topBar = {
            if (!isOnline && shouldShowBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AmberLight)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "● ऑफ़लाइन मोड: डेटा सुरक्षित है, इंटरनेट आने पर सिंक होगा",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AmberAlert,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        },
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
                    ratesFlow = container.priceRepository.observeTodayRates(),
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
                    ratesFlow = container.priceRepository.observeTodayRates(),
                    onVoiceSpeak = { audioPlayer.speak(it) }
                )
            }

            // 4. Lot Capture (Pushed Step 1)
            composable(Screen.LotCapture.route) {
                LotCaptureScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onProceedToEstimate = { _, weight, material ->
                        coroutineScope.launch {
                            val estimateResult = container.priceRepository.calculateEstimate(material, weight)
                            val createdLot = container.lotRepository.createDraftLot(
                                materialCategory = material,
                                weightKg = weight,
                                estimateRupees = estimateResult.totalEstimateRupees
                            )
                            navController.navigate(Screen.LotEstimate.createRoute(createdLot.id))
                        }
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
                    recyclersFlow = container.recyclerRepository.observeAllRecyclers(),
                    onNavigateBack = { navController.popBackStack() },
                    onRecyclerSelected = { recyclerId ->
                        coroutineScope.launch {
                            container.lotRepository.assignRecycler(lotId, recyclerId)
                            navController.navigate(Screen.Handover.createRoute(lotId))
                        }
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
                        coroutineScope.launch {
                            val signedRecord = container.recordSigner.signLot(
                                lotId = lotId,
                                collectorId = "COL-9821-4321",
                                material = "PET Plastic",
                                weightKg = 28.5,
                                ratePerKg = 20,
                                pickupLat = 19.0434,
                                pickupLng = 72.8562
                            )
                            container.ledgerRepository.recordVerifiedTransaction(
                                lotId = lotId,
                                recyclerId = "REC-MUM-01",
                                recyclerName = "EcoGreen Polymers",
                                materialCategory = "PET Plastic",
                                finalWeightKg = 28.5,
                                finalRatePerKg = 20,
                                totalAmountRupees = 570,
                                paymentMethod = "UPI",
                                collectorSignature = signedRecord.ed25519Signature,
                                recyclerSignature = "rec_sig_cpcb_${System.currentTimeMillis()}"
                            )
                            navController.navigate(Screen.Receipt.createRoute(lotId)) {
                                popUpTo(Screen.Home.route)
                            }
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
                    transactionsFlow = container.ledgerRepository.observeAllTransactions(),
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
