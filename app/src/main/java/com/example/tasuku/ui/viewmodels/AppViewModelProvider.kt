package com.example.tasuku.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasuku.TasukuApplication

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                getApplication().container.authenticationRepository,
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            RegisterViewModel(getApplication().container.authenticationRepository)
        }

        initializer {
            TaskViewModel(getApplication().container.taskRepository)
        }

        initializer {
            HomeViewModel(
                getApplication().container.workspaceRepository,
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            TaskDetailViewModel(
                getApplication().container.taskRepository,
                this.createSavedStateHandle(),
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            GroupViewModel(
                getApplication().container.groupRepository,
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            GroupDetailViewModel(
                getApplication().container.groupRepository,
                this.createSavedStateHandle(),
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            TaskAddAndUpdateViewModel(
                getApplication().container.groupRepository,
                getApplication().container.taskRepository,
                getApplication().container.sharedPreferences,
                this.createSavedStateHandle(),
            )
        }

        initializer {
            GroupMemberViewModel(
                getApplication().container.groupRepository,
                this.createSavedStateHandle(),
                getApplication().container.sharedPreferences
            )
        }

        initializer {
            ScheduleViewModel(
                getApplication().container.scheduleRepository,
            )
        }

        initializer {
            ProfileViewModel(
                getApplication().container.userRepository,
                getApplication().container.authenticationRepository
            )
        }

        initializer {
            MessageViewModel(this.createSavedStateHandle())
        }
    }
}

fun CreationExtras.getApplication(): TasukuApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TasukuApplication)