package com.autodash.feature.carapp

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

/**
 * Uma Screen devolve um Template; o HOST desenha. Nada de View/Compose aqui.
 * ListTemplate = lista dirigível. Mantemos CURTA: o host limita itens em movimento
 * (ConstraintManager) por distração (docs/03 + docs/05). invalidate() pediria refresh.
 */
class PoiScreen(ctx: CarContext) : Screen(ctx) {

    override fun onGetTemplate(): Template {
        val list = ItemList.Builder().apply {
            postosProximos().forEach { poi ->
                addItem(
                    Row.Builder()
                        .setTitle(poi.name)
                        .addText(poi.distance)
                        .setOnClickListener { /* navegar/ver detalhe */ }
                        .build()
                )
            }
        }.build()

        return ListTemplate.Builder()
            .setSingleList(list)
            .setTitle("Postos próximos")
            .setHeaderAction(Action.BACK)
            .build()
    }

    private data class Poi(val name: String, val distance: String)
    private fun postosProximos() = listOf(
        Poi("Posto Ipiranga", "1,2 km"),
        Poi("Shell Select", "2,8 km"),
        Poi("Eletroposto EDP", "3,5 km"),
    )
}
