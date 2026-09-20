package com.autodash.feature.carapp

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

/**
 * Entry point da Car App Library (registrado no AndroidManifest do app/, categoria POI).
 * O HOST do carro conecta aqui e renderiza os templates que as Screens descrevem.
 * Roda tanto em AAOS quanto em Android Auto. Ver docs/03.
 */
class AutoDashCarAppService : CarAppService() {

    override fun createHostValidator(): HostValidator =
        // Em produção, valide o host (allowlist). Simplificado para exemplo.
        HostValidator.ALLOW_ALL_HOSTS_VALIDATOR

    override fun onCreateSession(): Session = object : Session() {
        override fun onCreateScreen(intent: android.content.Intent) = PoiScreen(carContext)
    }
}
