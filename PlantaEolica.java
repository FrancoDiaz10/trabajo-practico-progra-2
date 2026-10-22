package ar.edu.ungs.prog2.smartgrids;

public class PlantaEolica extends PlantaEnergiaRenovable {

    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadMinimaViento;

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);
        if (cantidadAerogeneradores <= 0 || alturaTorres <= 0 || velocidadMinimaViento <= 0) {
            throw new RuntimeException("datos de planta eolica invalidos");
        }
        this.cantidadAerogeneradores = cantidadAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.velocidadMinimaViento = velocidadMinimaViento;
    }

    @Override
    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias) {
        throw new RuntimeException("una planta eolica requiere velocidad del viento");
    }

    @Override
    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        agregarMonitoreo(new MonitoreoEolica(fechaHora, energiaGenerada, incidencias, velocidadViento));
    }

    @Override
    public String reporteHorasConPocoViento() {
        double[] maxima = new double[24];
        boolean[] hayRegistro = new boolean[24];

        // mayor velocidad registrada en cada hora, entre todos los dias
        for (Monitoreo monitoreo : monitoreos.values()) {
            double velocidad = ((MonitoreoEolica) monitoreo).verVelocidadViento();
            int hora = monitoreo.verHora();
            if (!hayRegistro[hora] || velocidad > maxima[hora]) {
                maxima[hora] = velocidad;
                hayRegistro[hora] = true;
            }
        }

        StringBuilder bloque = new StringBuilder();
        bloque.append("- ").append(verNombre()).append(":  ")
                .append(formatear(velocidadMinimaViento)).append(" m/s\n");
        for (int hora = 0; hora < 24; hora++) {
            if (hayRegistro[hora] && maxima[hora] < velocidadMinimaViento) {
                bloque.append("        ").append(String.format("%02d", hora))
                        .append(": ").append(formatear(maxima[hora])).append(" m/s\n");
            }
        }
        return bloque.toString();
    }

    // 5.0 se imprime como "5", 4.5 queda "4.5"
    private String formatear(double valor) {
        if (valor == Math.floor(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    @Override
    public String toString() {
        return "[Eolica] " + super.toString() + " | aerogeneradores=" + cantidadAerogeneradores
                + " | alturaTorres=" + alturaTorres + " | velocidadMinima=" + velocidadMinimaViento;
    }
}
