package ar.edu.ungs.prog2.smartgrids;

public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento);
        if (tipoCombustible == null || tipoCombustible.isBlank()) {
            throw new RuntimeException("tipo de combustible invalido");
        }
        if (objetivoEmisionesCo2PorHora <= 0 || capacidadAlmacenamientoCombustible <= 0) {
            throw new RuntimeException("datos de planta termica invalidos");
        }
        this.tipoCombustible = tipoCombustible;
        this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
        this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
    }

    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        MonitoreoTermica termica = (MonitoreoTermica) monitoreo;
        return super.cumpleCuota(monitoreo) && termica.verCo2Emitido() < objetivoEmisionesCo2PorHora;
    }

    @Override
    public double capacidadMinimaCuota() {
        return verCapacidadMaxima() / 2;
    }

    @Override
    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias) {
        throw new RuntimeException("una planta termica requiere co2 y combustible");
    }

    @Override
    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        agregarMonitoreo(new MonitoreoTermica(fechaHora, energiaGenerada, incidencias, co2Emitido, combustibleUtilizado));
    }

    @Override
    public String toString() {
        return "[Termica] " + super.toString() + " | combustible=" + tipoCombustible
                + " | objetivoCo2/h=" + objetivoEmisionesCo2PorHora
                + " | almacenamiento=" + capacidadAlmacenamientoCombustible;
    }
}
