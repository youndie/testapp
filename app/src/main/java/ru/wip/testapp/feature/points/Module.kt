package ru.wip.testapp.feature.points

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.wip.testapp.data.coreModule
import ru.wip.testapp.data.ioDispatcher
import ru.wip.testapp.data.networkModule
import ru.wip.testapp.feature.points.data.PointsRepositoryImpl
import ru.wip.testapp.feature.points.data.ShareServiceImpl
import ru.wip.testapp.feature.points.domain.PointsRepository
import ru.wip.testapp.feature.points.domain.PointsViewModel
import ru.wip.testapp.feature.points.domain.ShareService

val pointsModule = module {
  includes(coreModule, networkModule)

  factory<ShareService> { ShareServiceImpl(androidContext(), get(qualifier = named(ioDispatcher))) }
  factory<PointsRepository> { PointsRepositoryImpl(get(), get(), get(), get(qualifier = named(ioDispatcher))) }

  viewModel { PointsViewModel(get(), get()) }
}