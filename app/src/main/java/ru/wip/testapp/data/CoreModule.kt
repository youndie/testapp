package ru.wip.testapp.data

import androidx.navigation.fragment.NavHostFragment
import org.koin.dsl.module
import ru.wip.testapp.domain.navigation.Navigator

val coreModule = module {
    factory<DataSource> { FileDataSource(get()) }
    single { Navigator() }
    factory { (navHostFragment: NavHostFragment) -> navHostFragment.navController }
}