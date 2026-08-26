package com.educalab.clasesmart.ui.navigation

import android.content.Context
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.educalab.clasesmart.di.AppContainer
import com.educalab.clasesmart.ui.screens.*
import com.educalab.clasesmart.ui.scene.ClassroomScene
import com.educalab.clasesmart.ui.viewmodel.*
import kotlinx.coroutines.flow.first

private const val PREFS_NAME = "clasesmart_prefs"
private const val PREF_AULA_GUIDE_SEEN = "aula_guide_seen"

@Composable
fun ClaseSmartNavGraph(container: AppContainer) {
    val navController: NavHostController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory(container.userProfileDao))
    val profileState by profileViewModel.uiState.collectAsState()

    if (profileState.isLoading) {
        CircularProgressIndicator()
        return
    }

    val startDestination = if (profileState.onboardingCompleted) ClaseSmartRoutes.AULA else ClaseSmartRoutes.ONBOARDING

    NavHost(navController = navController, startDestination = startDestination) {

        composable(ClaseSmartRoutes.ONBOARDING) {
            OnboardingScreen(profileViewModel) {
                navController.navigate(ClaseSmartRoutes.AULA) {
                    popUpTo(ClaseSmartRoutes.ONBOARDING) { inclusive = true }
                }
            }
        }

        composable(
            ClaseSmartRoutes.AULA,
            exitTransition = ClassroomTransitions.exitZoomOut,
            popEnterTransition = ClassroomTransitions.popEnterZoomBack
        ) {
            val vm: ClassroomViewModel = viewModel(factory = ClassroomViewModel.Factory(container.classroomRepository, container.progressRepository))
            val state by vm.uiState.collectAsState()
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
            var showGuide by remember { mutableStateOf(!prefs.getBoolean(PREF_AULA_GUIDE_SEEN, false)) }
            ClassroomScene(
                objects = state.objects,
                aulaLevel = state.aulaLevel,
                totalXp = state.totalXp,
                onObjectTap = { obj ->
                    val route = when (obj.type.name) {
                        "PIZARRA" -> ClaseSmartRoutes.ORGANIZA_DIA
                        "RELOJ" -> ClaseSmartRoutes.RELOJ_TIEMPO
                        "ESTANTE" -> ClaseSmartRoutes.MISION_MATERIALES
                        "PUPITRES" -> ClaseSmartRoutes.EQUIPOS
                        "BIBLIOTECA" -> ClaseSmartRoutes.BIBLIOTECA
                        "MOCHILA" -> ClaseSmartRoutes.PIZARRA_IDEAS
                        "PUERTA" -> ClaseSmartRoutes.PROYECTOS
                        "CARTEL" -> ClaseSmartRoutes.SITUACIONES
                        else -> null
                    }
                    route?.let { navController.navigate(it) }
                },
                showGuide = showGuide,
                onDismissGuide = {
                    showGuide = false
                    prefs.edit().putBoolean(PREF_AULA_GUIDE_SEEN, true).apply()
                },
                onOpenGuide = { showGuide = true }
            )
        }

        composable(
            ClaseSmartRoutes.ORGANIZA_DIA,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            val vm: OrganizaDiaViewModel = viewModel(
                factory = OrganizaDiaViewModel.Factory(container.timeBlockDao, container.classActivityDao, container.scheduleRepository, container.progressRepository, container.badgeRepository)
            )
            OrganizaDiaScreen(vm) { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.MISION_MATERIALES,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            val vm: MaterialesViewModel = viewModel(factory = MaterialesViewModel.Factory(container.materialRepository, container.progressRepository, container.badgeRepository))
            MisionMaterialesScreen(vm) { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.EQUIPOS,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            val vm: EquiposViewModel = viewModel(factory = EquiposViewModel.Factory(container.characterRepository, container.teamRepository, container.progressRepository, container.badgeRepository))
            EquiposScreen(vm) { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.SITUACIONES,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            val vm: SituacionesViewModel = viewModel(
                factory = SituacionesViewModel.Factory(container.situationRepository, container.progressRepository, container.badgeRepository)
            )
            SituacionesScreen(vm) { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.PIZARRA_IDEAS,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            PizarraIdeasScreen { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.BIBLIOTECA,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            BibliotecaScreen { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.RELOJ_TIEMPO,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) {
            val vm: RelojTiempoViewModel = viewModel(factory = RelojTiempoViewModel.Factory(container.progressRepository))
            RelojTiempoScreen(vm) { navController.popBackStack() }
        }

        composable(ClaseSmartRoutes.PROYECTOS) {
            var projects by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(listOf<com.educalab.clasesmart.data.local.entity.ProjectEntity>()) }
            LaunchedEffect(Unit) { projects = loadProjectsEntities(container) }
            ProyectosListScreen(projects, onSelect = { navController.navigate(ClaseSmartRoutes.proyecto(it)) }) { navController.popBackStack() }
        }

        composable(
            ClaseSmartRoutes.PROYECTO_DETALLE,
            enterTransition = ClassroomTransitions.enterZoomIn,
            popExitTransition = ClassroomTransitions.popExitZoomBack
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: return@composable
            val vm: ProyectoViewModel = viewModel(
                key = projectId,
                factory = ProyectoViewModel.Factory(projectId, container.projectRepository, container.progressRepository, container.badgeRepository)
            )
            ProyectoScreen(vm) { navController.popBackStack() }
        }

        composable(ClaseSmartRoutes.PERFIL) {
            PerfilScreen(profileViewModel) { navController.popBackStack() }
        }

        composable(ClaseSmartRoutes.INSIGNIAS) {
            var badges by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(listOf<com.educalab.clasesmart.data.local.entity.BadgeEntity>()) }
            val earned by container.badgeRepository.observeEarnedIds().collectAsState(initial = emptySet())
            LaunchedEffect(Unit) { badges = loadBadgesEntities(container) }
            InsigniasScreen(badges, earned) { navController.popBackStack() }
        }
    }
}

private suspend fun loadProjectsEntities(container: AppContainer) =
    container.projectRepository.getAllProjects().map { p ->
        com.educalab.clasesmart.data.local.entity.ProjectEntity(p.projectId, p.title, p.description)
    }

private suspend fun loadBadgesEntities(container: AppContainer): List<com.educalab.clasesmart.data.local.entity.BadgeEntity> =
    container.badgeDao.observeAll().first()
