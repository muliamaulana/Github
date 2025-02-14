package com.muliamaulana.github.di

import com.muliamaulana.github.data.GithubRepository
import com.muliamaulana.github.domain.repository.IGithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by muliamaulana on 14/02/25.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideGithubRepository(githubRepository: GithubRepository): IGithubRepository

}