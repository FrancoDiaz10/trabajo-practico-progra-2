package ar.edu.ungs.prog2.smartgrids;

import java.util.*;

public abstract class Planta {

    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private String fechaHoraPuestaEnFuncionamiento;
    private boolean activa = true;

    protected TreeMap<String, Monitoreo> monitoreos = new TreeMap<>();

    private List<PeriodoInactividad> periodosInactividad = new ArrayList<>();

    public Planta(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("nombre invalido");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new RuntimeException("direccion invalida");
        }
        if (capacidadMaxima <= 0) {
            throw new RuntimeException("capacidad invalida");
        }
        if (fechaHoraPuestaEnFuncionamiento == null || fechaHoraPuestaEnFuncionamiento.length() != 19) {
            throw new RuntimeException("fecha de puesta en funcionamiento invalida");
        }
        this.nombre = nombre;
        this.fechaHoraPuestaEnFuncionamiento = fechaHoraPuestaEnFuncionamiento;
        this.capacidadMaxima = capacidadMaxima;
        this.direccion = direccion;
    }

    public int limpiarMonitoreosSinDatos() {
        int borrados = 0;
        Iterator<Monitoreo> it = monitoreos.values().iterator();
        while (it.hasNext()) {
            Monitoreo monitoreo = it.next();
            if (monitoreo.verEnergiaGenerada() == 0 && monitoreo.verIncidencias() == 0) {
                it.remove();
                borrados++;
            }
        }
        return borrados;
    }

    public boolean esRenovable() {
        return false;
    }

    public boolean cumpleCuota(Monitoreo monitoreo) {
        return monitoreo.verEnergiaGenerada() >= capacidadMinimaCuota();
    }

    public int[] histogramaCumplimiento(String fechaDesde, String fechaHasta) {
        if (fechaDesde == null || fechaHasta == null || fechaDesde.compareTo(fechaHasta) > 0) {
            throw new RuntimeException("rangos de fechas invalidos");
        }
        int[] histograma = new int[24];
        String desde = fechaDesde + "T00:00:00";
        String hasta = fechaHasta + "T23:59:59";
        for (Monitoreo monitoreo : monitoreos.subMap(desde, true, hasta, true).values() ) {
           if (cumpleCuota(monitoreo)) {
               histograma[monitoreo.verHora()]++;
           }
        }
        return histograma;
    }

    public double totalGenerado() {
        double total = 0;
        for (Monitoreo monitoreo : monitoreos.values()) {
            total += monitoreo.verEnergiaGenerada();
        }
        return total;
    }

    public void desactivar(String fechaHora) {
        if (activa) {
            activa = false;
            periodosInactividad.add(new PeriodoInactividad(fechaHora));
        }
    }

    public void activar(String fechaHora) {
        if (!activa) {
            periodosInactividad.get(periodosInactividad.size() - 1).terminar(fechaHora);
            activa = true;
        }
    }

    public boolean estaActiva() {
        return activa;
    }

    public String verDetalle() {
        String estado;
        if (activa) {
            estado = "ACTIVA";
        } else {
            estado = "INACTIVA";
        }
        return nombre + " (" + estado + "): " + fechaHoraPuestaEnFuncionamiento;
    }

    public abstract double capacidadMinimaCuota();

    public double verCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias) {
        agregarMonitoreo(new MonitoreoComun(fechaHora, energiaGenerada, incidencias));
    }

    public void agregarMonitoreo(Monitoreo monitoreo) {
        String fechaHora = monitoreo.verFechaHora();
        if (!activa) {
            throw new RuntimeException("la planta esta inactiva");
        }
        if (fechaHora.compareTo(fechaHoraPuestaEnFuncionamiento) < 0) {
            throw new RuntimeException("el monitoreo es anterior a la fecha puesta en funcionamiento");
        }
        if (estuvoInactivaEn(fechaHora)) {
            throw new RuntimeException("la planta estaba en inactiva en la fecha y hora");
        }
        if (tieneMonitoreo(fechaHora)) {
            throw new RuntimeException("ya hay monitoreo");
        }
        monitoreos.put(fechaHora, monitoreo);
    }

    private boolean tieneMonitoreo(String fechaHora) {
        return monitoreos.containsKey(fechaHora);
    }

    public boolean estuvoInactivaEn(String fechaHora) {
        for (PeriodoInactividad periodo : periodosInactividad) {
            if (periodo.contiene(fechaHora)) {
                return true;
            }
        }
        return false;
    }

    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        throw new RuntimeException("la planta no registra velocidad de viento");
    }

    public void registrarMonitoreo(String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        throw new RuntimeException("la planta no registra co2 ni combustible");
    }

    public String reporteHorasConPocoViento() {
        return "";
    }

    public String verNombre() {
        return nombre;
    }

    public int[] incidenciasPorHora() {
        int[] porHora = new int[24];
        for (Monitoreo monitoreo : monitoreos.values()) {
            porHora[monitoreo.verHora()] += monitoreo.verIncidencias();
        }
        return porHora;
    }

    public boolean cumplioCuotaAlgunaVez() {
        for (Monitoreo monitoreo : monitoreos.values()) {
            if (cumpleCuota(monitoreo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return verDetalle() + " | direccion=" + direccion + " | capacidadMaxima=" + capacidadMaxima;
    }

    // periodos de inactividad y monitoreos, para el toString de SmartGrids
    public String verHistorial() {
        StringBuilder sb = new StringBuilder();
        sb.append("    Periodos de inactividad: ");
        if (periodosInactividad.isEmpty()) {
            sb.append("ninguno\n");
        } else {
            sb.append("\n");
            for (PeriodoInactividad periodo : periodosInactividad) {
                sb.append("      ").append(periodo).append("\n");
            }
        }
        sb.append("    Monitoreos: ");
        if (monitoreos.isEmpty()) {
            sb.append("ninguno\n");
        } else {
            sb.append("\n");
            for (Monitoreo monitoreo : monitoreos.values()) {
                sb.append("      ").append(monitoreo).append("\n");
            }
        }
        return sb.toString();
    }
}
