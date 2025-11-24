/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

import Main.config.Configuracion;

/**
 *
 * @author Josue
 */
public class GestorCajas {
    private Caja cajaPreferencial;
    private Caja cajaRapida;
    private Caja[] cajasNormales;

    public GestorCajas(Configuracion config) {
        this.cajaPreferencial = new Caja(config.getCajaPreferencial());
        this.cajaRapida = new Caja(config.getCajaRapida());

        int[] normales = config.getCajasNormales();
        cajasNormales = new Caja[normales.length];
        for (int i = 0; i < normales.length; i++) {
            cajasNormales[i] = new Caja(normales[i]);
        }
    }

    public Caja getCajaPreferencial() { 
        return cajaPreferencial; 
    }
    public Caja getCajaRapida() { 
        return cajaRapida; 
    }
    public Caja[] getCajasNormales() { 
        return cajasNormales; 
    }

    public Caja buscarCajaNormalConMenosClientes() {
        Caja menor = cajasNormales[0];
        for (Caja c : cajasNormales) {
            if (c.personasEnFila() < menor.personasEnFila()) {
                menor = c;
            }
        }
        return menor;
    }
}