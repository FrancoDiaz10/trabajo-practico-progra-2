package ar.edu.ungs.prog2.smartgrids;

public abstract class PlantaEnergiaRenovable extends Planta {

    private String fuentePrimaria;
    protected double eficienciaPromedio;

    public PlantaEnergiaRenovable(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento);
        if (fuentePrimaria == null || fuentePrimaria.isBlank()) {
            throw new RuntimeException("fuente primaria invalida");
        }
        if (eficienciaPromedio <= 0 || eficienciaPromedio > 1) {
            throw new RuntimeException("eficiencia invalida");
        }
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }

    @Override
    public double capacidadMinimaCuota() {
        return verCapacidadMaxima() * eficienciaPromedio;
    }

    @Override
    public boolean esRenovable() {
        return true;
    }

    public String verFuentePrimaria() {
        return fuentePrimaria;
    }

    @Override
    public String toString() {
        return super.toString() + " | fuente=" + fuentePrimaria + " | eficiencia=" + eficienciaPromedio;
    }
}
