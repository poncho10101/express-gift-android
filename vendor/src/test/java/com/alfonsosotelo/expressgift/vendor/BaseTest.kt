package com.alfonsosotelo.expressgift.vendor

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

abstract class BaseTest {

    @Mock
    lateinit var applicationMock: Application

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    open fun setUp() {
        MockitoAnnotations.initMocks(this)

    }
}