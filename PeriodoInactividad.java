package ar.edu.ungs.prog2.smartgrids;

public class PeriodoInactividad {

    private String desde;
    private String hasta;

    public PeriodoInactividad(String desde) {
        if (desde == null || desde.length() != 19) {
            throw new RuntimeException("fecha de inicio invalida");
        }
        this.desde = desde;
    }

    public void terminar(String hasta) {
        if (hasta == null || hasta.length() != 19 || hasta.compareTo(desde) < 0) {
            throw new RuntimeException("fecha de fin invalida");
        }
        this.hasta = hasta;
    }

    public boolean contiene(String fechaHora) {
        boolean esFechaDespues = fechaHora.compareTo(desde) >= 0;
        boolean esFechaHasta = hasta == null || fechaHora.compareTo(hasta) < 0;
        return esFechaDespues && esFechaHasta;
    }

    @Override
    public String toString() {
        return desde + " -> " + (hasta == null ? "(sigue inactiva)" : hasta);
    }
}
