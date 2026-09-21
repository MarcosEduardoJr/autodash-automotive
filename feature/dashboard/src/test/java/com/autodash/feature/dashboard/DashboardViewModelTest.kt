package com.autodash.feature.dashboard

import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import com.autodash.data.car.FakeCarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * TESTE UNITÁRIO (JVM) do DashboardViewModel — roda sem emulador, sem `android.car`, sem carro.
 *
 * MACRO (o conceito que este teste ensina):
 *  - MVVM + StateFlow: o VM expõe um `ui: StateFlow<DashboardUi>` IMUTÁVEL; a UI só lê.
 *    Aqui provamos o coração da camada: o VM MAPEIA os sinais do repositório (domínio) para o
 *    estado de tela. Inversão de dependência: o VM recebe um `CarRepository` (contrato do domínio),
 *    NÃO o CarPropertyManager do AAOS — por isso injetamos o `FakeCarRepository` e testamos na JVM.
 *  - viewModelScope roda em `Dispatchers.Main` → em teste TROCAMOS o Main por um
 *    `StandardTestDispatcher` (setMain/resetMain) para controlar o tempo virtual das corrotinas.
 *
 *  POR QUE **NÃO** usamos `runTest { ... }` aqui  <-- pegadinha clássica de coroutines-test:
 *  o `FakeCarRepository` expõe `vehicleSpeed()/energy()/rangeKm()/outsideTempC()` como laços
 *  `while (true) { emit(...); delay(...) }` (fluxos INFINITOS). O `init` do VM lança 5 coletores
 *  em `viewModelScope`, que NUNCA são cancelados (não há como cancelar o viewModelScope de fora).
 *  Ao terminar, o `runTest` faz um "drain" avançando o tempo virtual até esvaziar o scheduler —
 *  e como esses fluxos reagendam um `delay` para sempre, esse drain **nunca termina** (o teste
 *  trava). Solução: dirigir o scheduler à mão com `scheduler.runCurrent()`, que executa só o que já
 *  está pronto no tempo atual (a 1ª emissão de cada sinal, pois `emit` vem antes do `delay`) e para.
 *  Sem `runTest`, os coletores suspensos simplesmente ficam abandonados no fim do método — inofensivo.
 */
@OptIn(ExperimentalCoroutinesApi::class) // scheduler.runCurrent() ainda é API experimental
class DashboardViewModelTest {

    // Dispatcher de teste ÚNICO usado como Main (viewModelScope). Seu `scheduler` é o "relógio virtual".
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // viewModelScope usa Dispatchers.Main; sem device não há Main real → injetamos o de teste.
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        // Sempre restaurar o Main global — senão vaza o dispatcher de teste entre casos.
        Dispatchers.resetMain()
    }

    @Test
    fun mapeiaSinaisDoRepositorioParaOEstadoDeTela() {
        // MICRO: o primeiro estado de UI deve refletir a PRIMEIRA leitura de cada sinal —
        // gear=DRIVE, energy=Battery(82), rangeKm=418, tempExterna=22, speed=0.
        val car = FakeCarRepository()

        // O init do VM agenda 5 coletores em viewModelScope (StandardTestDispatcher não roda eager):
        // ficam AGENDADOS, ainda não executados.
        val vm = DashboardViewModel(car)

        // Executa só o que está pronto agora: cada coletor consome a 1ª emissão e suspende no delay.
        // (runCurrent, e não advanceUntilIdle, para não perseguir os fluxos infinitos do fake.)
        dispatcher.scheduler.runCurrent()

        val ui = vm.ui.value
        assertEquals(Gear.DRIVE, ui.gear)                 // gear() = flowOf(Gear.DRIVE) (finito)
        assertEquals(Energy.Battery(82), ui.energy)       // sealed Energy: 1ª emissão preserva o tipo
        assertEquals(418, ui.rangeKm)                     // RANGE_REMAINING (VHAL) → 418 km
        assertEquals(22, ui.outsideTempC)                 // ENV_OUTSIDE_TEMPERATURE → 22 °C
        assertEquals(VehicleSpeed(0f), ui.speed)          // 1ª emissão de velocidade = 0 m/s
    }
}
