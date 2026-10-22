package ar.edu.ungs.prog2.smartgrids;

import java.util.*;

public class SmartGrids implements ISmartGrids {

    private Map<String, Planta> plantas = new LinkedHashMap<>();



    private Planta buscarPlanta(String nombre) {
        Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("no existe la planta");
        }
        return planta;
    }

    private void validarFechaHora(String fechaHora) {
        if (fechaHora == null || fechaHora.length() != 19) {
            throw new RuntimeException("fecha invalida");
        }
    }

    private void agregarPlanta(Planta planta) {
        if (plantas.containsKey(planta.verNombre())) {
            throw new RuntimeException("la planta ya existe");
        }
        plantas.put(planta.verNombre(), planta);
    }

    @Override
    public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
        agregarPlanta(new PlantaSolar(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, superficieCubierta, cantidadPaneles, horasRadiacionDiaria));
    }

    @Override
    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
        agregarPlanta(new PlantaEolica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, cantidadAerogeneradores, alturaTorres, velocidadMinimaViento));
    }

    @Override
    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        agregarPlanta(new PlantaHidraulica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, cantidadTurbinas, capacidadEmbalse));
    }

    @Override
    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
        agregarPlanta(new PlantaTermica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible));
    }

    @Override
    public List<String> consultarPlantas() {
        List<String> detalles = new ArrayList<>();
        for (Planta planta : plantas.values()) {
            detalles.add(planta.verDetalle());
        }
        return detalles;
    }

    @Override
    public double capacidadMinimaCuota(String nombre) {
        Planta planta = buscarPlanta(nombre);
        return planta.capacidadMinimaCuota();
    }

    @Override
    public void activarPlanta(String nombre, String fechaHora) {
        validarFechaHora(fechaHora);
        buscarPlanta(nombre).activar(fechaHora);
    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {
        validarFechaHora(fechaHora);
        buscarPlanta(nombre).desactivar(fechaHora);
    }

    @Override
    public boolean estaActiva(String nombre) {
        return buscarPlanta(nombre).estaActiva();
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
        validarFechaHora(fechaHora);
        buscarPlanta(nombre).registrarMonitoreo(fechaHora, energiaGenerada, incidencias);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        validarFechaHora(fechaHora);
        buscarPlanta(nombre).registrarMonitoreo(fechaHora, energiaGenerada, incidencias, velocidadViento);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        validarFechaHora(fechaHora);
        buscarPlanta(nombre).registrarMonitoreo(fechaHora, energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
    }

    @Override
    public double totalGenerado(String nombre) {
        return buscarPlanta(nombre).totalGenerado();
    }

    @Override
    public int[] incidenciasPorHora(String nombre) {
        return buscarPlanta(nombre).incidenciasPorHora();
    }

    @Override
    public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
        return buscarPlanta(nombre).histogramaCumplimiento(fechaDesde, fechaHasta);
    }

    @Override
    public Map<String, Integer> plantasRenovablesPorFuente() {
        Map<String, Integer> resultado = new TreeMap<>();
        for (Planta planta : plantas.values()) {
            if (planta.esRenovable()) {
                String fuente = ((PlantaEnergiaRenovable) planta).verFuentePrimaria();
                resultado.merge(fuente, 1, Integer::sum);
            }
        }
        return resultado;
    }

    @Override
    public List<String> plantasQueNuncaCumplieronCuota() {
        List<String> nuncaCumplieron = new ArrayList<>();
        for (Planta planta : plantas.values()) {
            if (!planta.cumplioCuotaAlgunaVez()) {
                nuncaCumplieron.add(planta.verNombre());
            }
        }
        return nuncaCumplieron;
    }

    @Override
    public String horasSinVelocidadMinimaPorPlantaEolica() {
        StringBuilder reporte = new StringBuilder("Plantas Eolicas: Horas con poco viento\n");
        for (Planta planta : plantas.values()) {
            reporte.append(planta.reporteHorasConPocoViento());
        }
        return reporte.toString();
    }

    @Override
    public int limpiarMonitoreosSinDatos() {
        int total = 0;
        for (Planta planta : plantas.values()) {
            total += planta.limpiarMonitoreosSinDatos();
        }
        return total;
    }

    @Override
    public List<String> plantasOrdenadasPorEnergiaGenerada() {
        List<Planta> ordenadas = new ArrayList<>(plantas.values());
        ordenadas.sort((a, b) -> Double.compare(b.totalGenerado(), a.totalGenerado()));

        List<String> resultado = new ArrayList<>();
        for (Planta planta : ordenadas) {
            resultado.add(planta.verNombre() + ": " + planta.totalGenerado());
        }
        return resultado;
    }

    @Override
    public List<String> plantasInactivasEn(String fechaHora) {
       validarFechaHora(fechaHora);
       List<String> inactivas = new ArrayList<>();
       for (Planta planta : plantas.values()) {
           if (planta.estuvoInactivaEn(fechaHora)) {
               inactivas.add(planta.verNombre());
           }
       }
       return inactivas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SmartGrids - plantas registradas: ");
        sb.append(plantas.size()).append("\n");
        for (Planta planta : plantas.values()) {
            sb.append("  ").append(planta).append("\n");
            sb.append(planta.verHistorial());
        }
        return sb.toString();
    }
}
