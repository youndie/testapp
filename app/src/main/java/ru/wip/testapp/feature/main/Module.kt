package ru.wip.testapp.feature.main

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.wip.testapp.feature.main.domain.MainViewModel
import ru.wip.testapp.feature.points.pointsModule

val mainModule = module {
    includes(pointsModule)

    viewModel { MainViewModel(get(), get(), get()) }
}