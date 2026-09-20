package com.autodash.feature.carapp

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

/**
 * Detalhe de um POI (PaneTemplate) — empilhado pela PoiScreen.
 * Mostra endereço/distância + ação "Navegar". BACK volta (ScreenManager.pop).
 * Em produção, "Navegar" entregaria um Trip ao NavigationManager (docs/08 · nav).
 */
class PoiDetailScreen(ctx: CarContext, private val poi: Poi) : Screen(ctx) {

    override fun onGetTemplate(): Template {
        val pane = Pane.Builder()
            .addRow(Row.Builder().setTitle("Endereço").addText(poi.address).build())
            .addRow(Row.Builder().setTitle("Distância").addText(poi.distance).build())
            .addAction(
                Action.Builder()
                    .setTitle("Navegar")
                    .setOnClickListener {
                        CarToast.makeText(carContext, "Iniciando navegação…", CarToast.LENGTH_SHORT).show()
                    }
                    .build()
            )
            .build()

        return PaneTemplate.Builder(pane)
            .setTitle(poi.name)
            .setHeaderAction(Action.BACK)
            .build()
    }
}
