package ru.wip.testapp.data

import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.wip.testapp.domain.navigation.Navigator

val coreModule = module {
  factory<DataSource> { FileDataSource(get(), get(qualifier = named(ioDispatcher))) }
  single { Navigator() }
  factory { (navHostFragment: NavHostFragment) -> navHostFragment.navController }
  single<CoroutineDispatcher>(named(ioDispatcher)) { Dispatchers.IO }
}

const val ioDispatcher = "ioDispatcher"