package com.muliamaulana.github.di

import com.muliamaulana.github.domain.usecase.GithubInteractor
import com.muliamaulana.github.domain.usecase.GithubUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by muliamaulana on 14/02/25.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideGithubUseCase(githubInteractor: GithubInteractor): GithubUseCase

}