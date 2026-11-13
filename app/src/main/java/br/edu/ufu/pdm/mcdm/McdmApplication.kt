package br.edu.ufu.pdm.mcdm

import android.app.Application
import br.edu.ufu.pdm.mcdm.di.AppContainer

class McdmApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
