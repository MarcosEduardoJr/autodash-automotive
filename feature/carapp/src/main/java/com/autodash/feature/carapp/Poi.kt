package com.autodash.feature.carapp

/** Ponto de interesse (dado). Em produção viria de um repo/rede via domain. */
data class Poi(val name: String, val distance: String, val address: String)

/** Lista de exemplo (postos/eletropostos próximos). */
val demoPois: List<Poi> = listOf(
    Poi("Posto Ipiranga", "1,2 km", "Av. Brasil, 1200"),
    Poi("Shell Select", "2,8 km", "Rod. Anchieta, km 23"),
    Poi("Eletroposto EDP", "3,5 km", "Rua das Palmeiras, 45"),
    Poi("BR Mania", "4,1 km", "Av. Faria Lima, 980"),
)
