package com.autodash.feature.carapp

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.DateTimeWithZone
import androidx.car.app.model.Distance
import androidx.car.app.model.Template
import androidx.car.app.navigation.NavigationManager
import androidx.car.app.navigation.NavigationManagerCallback
import androidx.car.app.navigation.model.Destination
import androidx.car.app.navigation.model.Maneuver
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.car.app.navigation.model.RoutingInfo
import androidx.car.app.navigation.model.Step
import androidx.car.app.navigation.model.TravelEstimate
import androidx.car.app.navigation.model.Trip
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.TimeZone

/**
 * Navegação turn-by-turn (categoria NAVIGATION da Car App Library).
 * - `NavigationTemplate` mostra a manobra/step atual (o HOST desenha; o mapa iria numa Surface).
 * - `NavigationManager` recebe o `Trip` (destino + step + ETA) — é o que o host projeta no
 *   INSTRUMENT CLUSTER (segunda tela). Ver docs/03 e docs/08.
 * navManager é lazy p/ não resolver o serviço fora do host (ex.: em teste de template).
 */
class NavScreen(ctx: CarContext, private val poi: Poi) : Screen(ctx) {

    private val navManager by lazy { carContext.getCarService(NavigationManager::class.java) }

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                navManager.setNavigationManagerCallback(object : NavigationManagerCallback {
                    override fun onStopNavigation() {
                        navManager.navigationEnded()
                        screenManager.pop()
                    }
                    override fun onAutoDriveEnabled() {}
                })
                navManager.navigationStarted()
                navManager.updateTrip(buildTrip())   // alimenta o cluster
            }
            override fun onPause(owner: LifecycleOwner) {
                navManager.navigationEnded()
                navManager.clearNavigationManagerCallback()
            }
        })
    }

    private fun eta(minAhead: Int) = DateTimeWithZone.create(
        System.currentTimeMillis() + minAhead * 60_000L, TimeZone.getDefault(),
    )

    private fun buildTrip(): Trip {
        val estimate = TravelEstimate.Builder(
            Distance.create(1.2, Distance.UNIT_KILOMETERS), eta(15),
        ).build()
        val step = Step.Builder("Vire à direita em 300 m")
            .setManeuver(Maneuver.Builder(Maneuver.TYPE_TURN_NORMAL_RIGHT).build())
            .build()
        val destination = Destination.Builder()
            .setName(poi.name).setAddress(poi.address).build()
        return Trip.Builder()
            .addDestination(destination, estimate)
            .addStep(step, estimate)
            .build()
    }

    override fun onGetTemplate(): Template {
        val info = RoutingInfo.Builder()
            .setCurrentStep(
                Step.Builder("Vire à direita em 300 m")
                    .setManeuver(Maneuver.Builder(Maneuver.TYPE_TURN_NORMAL_RIGHT).build())
                    .build(),
                Distance.create(300.0, Distance.UNIT_METERS),
            )
            .build()
        return NavigationTemplate.Builder()
            .setNavigationInfo(info)
            .setActionStrip(
                ActionStrip.Builder().addAction(
                    Action.Builder().setTitle("Encerrar").setOnClickListener {
                        navManager.navigationEnded()
                        screenManager.pop()
                    }.build(),
                ).build(),
            )
            .build()
    }
}
