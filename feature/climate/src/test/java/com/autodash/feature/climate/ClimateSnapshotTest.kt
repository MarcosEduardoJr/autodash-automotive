package com.autodash.feature.climate

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.designsystem.Brands
import com.autodash.core.model.Climate
import org.junit.Rule
import org.junit.Test

/**
 * Snapshot da tela de CLIMA (HVAC por zona) com Paparazzi.
 *
 * MACRO — SNAPSHOT TESTING no Android Automotive: Paparazzi renderiza o Compose no JVM (sem
 * emulador/carro) e compara o PNG gerado com uma imagem-referência versionada no git. Isso pega
 * REGRESSÃO VISUAL — cor, layout, alfa de estado desabilitado — em CI, sem precisar bootar o AAOS
 * nem plugar a head unit. É o complemento visual dos testes de unidade: o teste de VM prova a
 * LÓGICA, o snapshot prova o que o motorista de fato ENXERGA.
 */
class ClimateSnapshotTest {
    // DeviceConfig.PIXEL_C = tela grande/landscape, próxima de uma head unit automotiva.
    @get:Rule val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_C)

    /**
     * MICRO: renderiza a tela em duas marcas (Slate e Aurora) com as mesmas temperaturas.
     * MACRO: prova o DYNAMIC THEMING white-label — a mesma ClimateScreen, apenas trocando os
     * BrandTokens, produz duas identidades visuais distintas. Um snapshot por marca (climate_slate,
     * climate_aurora) guarda cada aparência contra regressão.
     */
    @Test fun climate_multiBrand() {
        listOf(Brands.Slate, Brands.Aurora).forEach { b ->
            paparazzi.snapshot(name = "climate_${b.id}") {
                AutoDashTheme(b) { ClimateScreen(Climate(driverC = 22f, passengerC = 20f)) }
            }
        }
    }

    /**
     * MICRO: renderiza a tela com POWER desligado (powerOn=false, acOn=false) e captura o snapshot.
     * MACRO: prova visualmente o GATING de estado — com o sistema desligado, as zonas de temperatura,
     * o A/C e o FAN aparecem APAGADOS (alfa reduzido) e DESABILITADOS. Esse feedback "tudo apagado"
     * é um requisito de UX automotiva (o motorista precisa perceber num relance que o HVAC está off);
     * o snapshot congela esse visual desabilitado sem precisar abrir o carro.
     */
    @Test fun climate_power_off() {
        paparazzi.snapshot(name = "climate_power_off") {
            AutoDashTheme(Brands.Slate) { ClimateScreen(Climate(powerOn = false, acOn = false)) }
        }
    }

    /**
     * MICRO: renderiza com POWER ligado mas A/C desligado (acOn=false, powerOn=true default).
     * MACRO: prova o estado INTERMEDIÁRIO do gating — POWER on mantém zonas e FAN habilitados/acesos,
     * enquanto só o tile de A/C reflete o desligado. Comparado ao climate_power_off, este snapshot
     * garante que o gating é POR CONTROLE (não um blecaute geral), pegando regressão visual do estado
     * desabilitado de um único componente sem abrir o carro.
     */
    @Test fun climate_ac_off() {
        paparazzi.snapshot(name = "climate_ac_off") {
            AutoDashTheme(Brands.Slate) { ClimateScreen(Climate(acOn = false)) }
        }
    }
}
