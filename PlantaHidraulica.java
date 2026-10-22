package ar.edu.ungs.prog2.smartgrids;

public class PlantaHidraulica extends PlantaEnergiaRenovable {

    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);
        if (cantidadTurbinas <= 0 || capacidadEmbalse <= 0) {
            throw new RuntimeException("datos de planta hidraulica invalidos");
        }
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    @Override
    public String toString() {
        return "[Hidraulica] " + super.toString() + " | turbinas=" + cantidadTurbinas
                + " | embalse=" + capacidadEmbalse;
    }
}
