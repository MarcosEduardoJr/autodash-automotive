package com.autodash.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * MACRO (conceito que ensina): modelo de dominio PURO = testavel sem emulador.
 *
 * `Climate` e `Seat` vivem em `core:model`, o anel mais interno da Clean
 * Architecture. Nao dependem de Android, da Car API nem de corrotinas — sao
 * apenas dados + uma funcao. Por isso este arquivo e um teste JVM puro
 * (`src/test`, roda em milissegundos, sem Robolectric, sem `@RunWith`, sem
 * device). A regra da Clean Architecture: quanto mais interna a camada, mais
 * barato e rapido o teste. Se um dia `temp()` ganhar uma regra de negocio
 * (ex.: limitar zonas), ela e coberta aqui, longe do AAOS.
 *
 * Nota de detalhe JUnit4: comparar `Float` exige delta (o overload
 * `assertEquals(Float, Float)` sem delta e deprecado por causa de erro de
 * ponto flutuante). Usamos `DELTA = 0f` porque os valores sao literais exatos,
 * mas deixamos o parametro explicito para ensinar o padrao correto.
 */
class ClimateTest {

    /** Tolerancia para comparacao de ponto flutuante (0f: valores exatos). */
    private val delta = 0f

    /**
     * MICRO: um `Climate()` sem argumentos traz os defaults do painel
     * (motorista 21°C, passageiro 21°C, ligado, A/C ligado, ventilacao 3).
     * MACRO: defaults no data class = "estado inicial seguro". A UI pode
     * renderizar antes de o carro emitir qualquer valor, sem null nem crash.
     */
    @Test
    fun defaults_saoOEstadoInicialSeguro() {
        val c = Climate()
        assertEquals(21f, c.driverC, delta)
        assertEquals(21f, c.passengerC, delta)
        assertTrue(c.powerOn)
        assertTrue(c.acOn)
        assertEquals(3, c.fanSpeed)
    }

    /**
     * MICRO: apos `copy(driverC = 24f, passengerC = 19f)`, `temp(DRIVER)`
     * devolve 24f e `temp(PASSENGER)` devolve 19f.
     * MACRO: `temp(seat)` e a unica logica do modelo — mapeia a zona (Seat)
     * para o campo certo. Testar essa funcao prova que o roteamento
     * motorista/passageiro esta correto sem precisar do HVAC real do veiculo.
     */
    @Test
    fun temp_roteiaCadaZonaParaSeuCampo() {
        val c = Climate().copy(driverC = 24f, passengerC = 19f)
        assertEquals(24f, c.temp(Seat.DRIVER), delta)
        assertEquals(19f, c.temp(Seat.PASSENGER), delta)
    }

    /**
     * MICRO: `copy()` alterando apenas as temperaturas preserva
     * `powerOn`, `acOn` e `fanSpeed` intactos.
     * MACRO: imutabilidade + `copy()` e como o estado flui num app Compose /
     * StateFlow. Ao inves de mutar, criamos um novo `Climate` mudando um campo
     * e herdando o resto. Este teste garante esse contrato do data class —
     * base de toda atualizacao de UI no `ClimateViewModel`.
     */
    @Test
    fun copy_preservaOsDemaisCampos() {
        val original = Climate(
            driverC = 20f,
            passengerC = 22f,
            powerOn = false,
            acOn = false,
            fanSpeed = 5,
        )

        val alterado = original.copy(driverC = 24f, passengerC = 19f)

        // Campos alterados
        assertEquals(24f, alterado.driverC, delta)
        assertEquals(19f, alterado.passengerC, delta)
        // Campos herdados sem tocar
        assertFalse(alterado.powerOn)
        assertFalse(alterado.acOn)
        assertEquals(5, alterado.fanSpeed)
    }
}
