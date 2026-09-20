package com.autodash.feature.carapp

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

/**
 * Tela de POI (ListTemplate). O HOST desenha; nada de View/Compose.
 * NAVEGÁVEL: clicar num item empilha a tela de detalhe (ScreenManager.push) —
 * fluxo raso, itens limitados pelo host em movimento (distração, docs/03 + docs/05).
 */
class PoiScreen(ctx: CarContext) : Screen(ctx) {

    override fun onGetTemplate(): Template {
        val items = ItemList.Builder().apply {
            demoPois.forEach { poi ->
                addItem(
                    Row.Builder()
                        .setTitle(poi.name)
                        .addText(poi.distance)
                        .setOnClickListener { screenManager.push(PoiDetailScreen(carContext, poi)) }
                        .setBrowsable(true)
                        .build()
                )
            }
        }.build()

        return ListTemplate.Builder()
            .setSingleList(items)
            .setTitle("Postos próximos")
            .setHeaderAction(Action.APP_ICON)
            .build()
    }
}
