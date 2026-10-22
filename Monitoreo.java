package ar.edu.ungs.prog2.smartgrids;

public abstract class Monitoreo {

    private String fechaHora;
    private double energiaGenerada;
    private int incidencias;

    public Monitoreo(String fechaHora, double energiaGenerada, int incidencias) {
        if (fechaHora == null || fechaHora.length() != 19) {
            throw new RuntimeException("fecha y hora invalida");
        }
        if (energiaGenerada < 0 || incidencias < 0) {
            throw new RuntimeException("energia o incidencias negativas");
        }
       this.fechaHora = fechaHora;
       this.energiaGenerada = energiaGenerada;
       this.incidencias = incidencias;
    }

    public int verIncidencias() {
        return incidencias;
    }

    public int verHora() {
        return Integer.parseInt(fechaHora.substring(11, 13));
    }

    public String verFechaHora() {
        return fechaHora;
    }

    public double verEnergiaGenerada() {
        return energiaGenerada;
    }

    @Override
    public String toString() {
        return fechaHora + " | energia=" + energiaGenerada + " | incidencias=" + incidencias;
    }

}
