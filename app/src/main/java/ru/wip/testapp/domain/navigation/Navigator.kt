package ru.wip.testapp.domain.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class Navigator {

    private val commands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)

    val observe: Flow<NavigationCommand> = commands

    fun navigateTo(screen: String) {
        commands.tryEmit(NavigationCommand.NavigateTo(screen))
    }

    fun back() {
        commands.tryEmit(NavigationCommand.Back())
    }

}