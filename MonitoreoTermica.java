package ar.edu.ungs.prog2.smartgrids;

public class MonitoreoTermica extends Monitoreo {

    private double co2Emitido;
    private double combustibleUtilizado;

    public MonitoreoTermica(String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        super(fechaHora, energiaGenerada, incidencias);
        if (co2Emitido < 0 || combustibleUtilizado < 0) {
            throw new RuntimeException("co2 o combustible negativos");
        }
        this.co2Emitido = co2Emitido;
        this.combustibleUtilizado = combustibleUtilizado;
    }

    public double verCo2Emitido() {
        return co2Emitido;
    }

    @Override
    public String toString() {
        return super.toString() + " | co2=" + co2Emitido + " | combustible=" + combustibleUtilizado;
    }
}
