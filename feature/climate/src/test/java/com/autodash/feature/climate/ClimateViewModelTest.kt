package com.autodash.feature.climate

import com.autodash.core.model.Seat
import com.autodash.data.car.FakeCarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * TESTE UNITÁRIO (JVM, sem device) do [ClimateViewModel] — HVAC por zona.
 *
 * MACRO — o que este arquivo ensina sobre testar ViewModel em Android Automotive:
 *  • Um ViewModel roda em `viewModelScope`, que despacha em `Dispatchers.Main`. Num teste puro
 *    de JVM não há Looper de Android, então trocamos o Main por um [StandardTestDispatcher]
 *    (Dispatchers.setMain no @Before / resetMain no @After). Assim controlamos o "relógio"
 *    das corrotinas manualmente com [advanceUntilIdle].
 *  • O StateFlow `ui` usa `SharingStarted.WhileSubscribed`: o fluxo do repositório só é coletado
 *    ENQUANTO houver assinante. Logo, em teste, precisamos de um coletor ativo (backgroundScope)
 *    antes de ler `ui.value`; sem ele o valor nunca sai do inicial. É o mesmo motivo pelo qual
 *    a tela precisa estar visível para a UI receber updates em produção.
 *  • Usamos o [FakeCarRepository] — mesmo contrato `CarRepository` do carro real. Testamos a
 *    lógica do ViewModel sem CarPropertyManager, sem permissões e sem emulador (inversão de
 *    dependência: o domínio depende do contrato, não do VHAL).
 */
class ClimateViewModelTest {

    // Um único dispatcher/scheduler compartilhado entre o Main (viewModelScope) e o runTest,
    // para que advanceUntilIdle() controle TODAS as corrotinas do teste num só relógio virtual.
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // MACRO: substitui Dispatchers.Main pelo dispatcher de teste — viewModelScope depende dele.
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        // MACRO: devolve o Main real. Obrigatório para não vazar o dispatcher de teste entre casos.
        Dispatchers.resetMain()
    }

    /**
     * HELPER — ativa o StateFlow `ui`.
     *
     * MACRO: como `ui` é `WhileSubscribed`, sem coletor o upstream (`car.climate()`) nunca liga e
     * `ui.value` fica preso no valor inicial. Lançamos um coletor no `backgroundScope` (que o
     * runTest cancela sozinho no fim — por isso não trava mesmo o Flow nunca "completando") e o
     * `advanceUntilIdle()` do chamador faz a assinatura de fato acontecer. É seguro porque o
     * `climate()` do fake é um MutableStateFlow FINITO (sem laço `while(true){ emit; delay }`),
     * diferente de speed/energy/range/temp — nesses, advanceUntilIdle() rodaria para sempre.
     */
    private fun TestScope.subscribeUi(vm: ClimateViewModel) {
        backgroundScope.launch { vm.ui.collect { } }
    }

    @Test
    fun estadoInicial_refleteOsDefaultsDoClima() = runTest(dispatcher) {
        // MICRO: sem nenhum comando, `ui` expõe o Climate() padrão (powerOn/acOn ligados, fan=3, 21°C).
        // MACRO: o valor inicial do stateIn coincide com o default do MutableStateFlow do carro —
        //        fonte única de verdade; a UI apenas espelha o estado do veículo.
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        val ui = vm.ui.value
        assertTrue("HVAC deve iniciar ligado", ui.powerOn)
        assertTrue("A/C deve iniciar ligado", ui.acOn)
        assertEquals("ventilação padrão", 3, ui.fanSpeed)
        assertEquals("temperatura do motorista padrão", 21f, ui.driverC, 0.0001f)
    }

    @Test
    fun togglePower_desligaOHvac() = runTest(dispatcher) {
        // MICRO: powerOn true -> false após togglePower().
        // MACRO: escreve HVAC_POWER_ON no VHAL; o comando vai ao repo e VOLTA pelo Flow até a UI
        //        (unidirecional: comando desce, estado sobe — a UI nunca guarda estado próprio).
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        vm.togglePower()
        advanceUntilIdle()

        assertFalse(vm.ui.value.powerOn)
    }

    @Test
    fun toggleAc_desligaOArCondicionado() = runTest(dispatcher) {
        // MICRO: acOn true -> false após toggleAc().
        // MACRO: HVAC_AC_ON é uma propriedade INDEPENDENTE de HVAC_POWER_ON no VHAL — alternar o
        //        A/C não altera o power (cada propriedade do carro é atômica e separada).
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        vm.toggleAc()
        advanceUntilIdle()

        assertFalse(vm.ui.value.acOn)
    }

    @Test
    fun fan_incrementaESaturaEmSeis() = runTest(dispatcher) {
        // MICRO: fan(+2): 3 -> 5; depois fan(+10): 5+10=15, mas o resultado satura em 6.
        // MACRO: HVAC_FAN_SPEED tem faixa 0..6 no VHAL; o repo aplica coerceIn(0, 6) — clamp
        //        defensivo para nunca mandar um valor fora do range físico do ventilador.
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        vm.fan(2)
        advanceUntilIdle()
        assertEquals("3 + 2", 5, vm.ui.value.fanSpeed)

        vm.fan(10)
        advanceUntilIdle()
        assertEquals("clamp no máximo da faixa (6)", 6, vm.ui.value.fanSpeed)
    }

    @Test
    fun delta_ajustaApenasOAssentoAlvo() = runTest(dispatcher) {
        // MICRO: delta(DRIVER, +1.5) -> motorista 22.5°C; passageiro permanece 21°C.
        // MACRO: cada assento é uma AREA (VehicleAreaSeat). HVAC_TEMPERATURE_SET é escrito POR ZONA,
        //        então o controle do motorista é totalmente independente do passageiro.
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        vm.delta(Seat.DRIVER, 1.5f)
        advanceUntilIdle()

        assertEquals("motorista 21 + 1.5", 22.5f, vm.ui.value.driverC, 0.0001f)
        assertEquals("passageiro não é afetado (zona independente)", 21f, vm.ui.value.passengerC, 0.0001f)
    }

    @Test
    fun delta_saturaEmVinteEOitoGraus() = runTest(dispatcher) {
        // MICRO: partindo de 28°C, delta(+5) tentaria 33°C, mas satura em 28°C.
        // MACRO: faixa segura do VHAL (16..28°C) aplicada por coerceIn no repo — a UI nunca
        //        comanda uma temperatura fora do range físico do climatizador.
        val vm = ClimateViewModel(FakeCarRepository())
        subscribeUi(vm)
        advanceUntilIdle()

        vm.delta(Seat.DRIVER, 7f)   // 21 -> 28 (topo da faixa)
        advanceUntilIdle()
        assertEquals("chegou ao teto da faixa", 28f, vm.ui.value.driverC, 0.0001f)

        vm.delta(Seat.DRIVER, 5f)   // 28 + 5 = 33 -> coerceIn satura
        advanceUntilIdle()
        assertEquals("clamp no teto de 28°C", 28f, vm.ui.value.driverC, 0.0001f)
    }
}
