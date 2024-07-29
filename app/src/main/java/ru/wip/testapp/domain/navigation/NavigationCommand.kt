package ru.wip.testapp.domain.navigation

sealed class NavigationCommand {
    class NavigateTo(val screen: String) : NavigationCommand()
    class Back : NavigationCommand()
}