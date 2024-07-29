package ru.wip.testapp.feature.points

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.wip.testapp.data.coreModule
import ru.wip.testapp.data.networkModule
import ru.wip.testapp.feature.points.data.PointsRepositoryImpl
import ru.wip.testapp.feature.points.data.ShareServiceImpl
import ru.wip.testapp.feature.points.domain.PointsRepository
import ru.wip.testapp.feature.points.domain.PointsViewModel
import ru.wip.testapp.feature.points.domain.ShareService

val pointsModule = module {
    includes(coreModule, networkModule)

    factory<ShareService> { ShareServiceImpl(androidContext()) }
    factory<PointsRepository> { PointsRepositoryImpl(get(), get(), get()) }

    viewModel { PointsViewModel(get(), get()) }
}