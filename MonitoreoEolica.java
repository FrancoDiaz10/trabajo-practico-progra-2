package ar.edu.ungs.prog2.smartgrids;

public class MonitoreoEolica extends Monitoreo {

    private double velocidadViento;

    public MonitoreoEolica(String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        super(fechaHora, energiaGenerada, incidencias);
        if (velocidadViento < 0) {
            throw new RuntimeException("velocidad de viento negativa");
        }
        this.velocidadViento = velocidadViento;
    }

    public double verVelocidadViento() { return velocidadViento; }

    @Override
    public String toString() {
        return super.toString() + " | viento=" + velocidadViento + " m/s";
    }


}
