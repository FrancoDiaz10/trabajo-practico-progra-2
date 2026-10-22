package ar.edu.ungs.prog2.smartgrids;

public class PlantaSolar extends PlantaEnergiaRenovable {

    private int cantidadPaneles;
    private double horasRadiacionDiaria;
    private double superficieCubierta;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria , eficienciaPromedio);
        if (superficieCubierta <= 0 || cantidadPaneles <= 0 || horasRadiacionDiaria <= 0 || horasRadiacionDiaria > 24) {
            throw new RuntimeException("datos de planta solar invalidos");
        }
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
        this.superficieCubierta = superficieCubierta;
    }

    @Override
    public double capacidadMinimaCuota() {
        return verCapacidadMaxima() * eficienciaPromedio * horasRadiacionDiaria / 24;
    }

    @Override
    public String toString() {
        return "[Solar] " + super.toString() + " | superficie=" + superficieCubierta
                + " | paneles=" + cantidadPaneles + " | horasRadiacion=" + horasRadiacionDiaria;
    }

}
