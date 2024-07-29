package ru.wip.testapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import ru.wip.testapp.feature.main.mainModule
import ru.wip.testapp.feature.points.pointsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(pointsModule, mainModule)
        }
    }
}
