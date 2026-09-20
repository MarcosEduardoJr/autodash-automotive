package com.autodash.app

import android.app.Application

/**
 * Application do processo.
 *
 * REGRA AUTOMOTIVA: onCreate MÍNIMO. O head unit boota com a ignição e o cold start
 * conta — trabalho pesado (analytics, sync) vai lazy/async ou para o garage mode
 * via JobScheduler (ver docs/07). Nada de I/O de rede aqui.
 */
class AutoDashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // init leve apenas. (DI seria configurado aqui.)
    }
}
