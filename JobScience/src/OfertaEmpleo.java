import java.util.List;
import java.util.Locale;

public class OfertaEmpleo {

    private String titulo;
    private String seniority;
    private String modalidad;
    private String empresa;
    private String ubicacion;
    private String fechaPublicacion;
    private String sede;
    private String industria;
    private String propiedad;
    private String tamanoEmpresa;
    private String ingresos;
    private double salarioMin;
    private double salarioMax;
    private List<String> habilidades;

    public OfertaEmpleo(String titulo, String seniority, String modalidad,
                        String empresa, String ubicacion, String fechaPublicacion,
                        String sede, String industria, String propiedad,
                        String tamanoEmpresa, String ingresos,
                        double salarioMin, double salarioMax,
                        List<String> habilidades) {

        this.titulo = titulo;
        this.seniority = seniority;
        this.modalidad = modalidad;
        this.empresa = empresa;
        this.ubicacion = ubicacion;
        this.fechaPublicacion = fechaPublicacion;
        this.sede = sede;
        this.industria = industria;
        this.propiedad = propiedad;
        this.tamanoEmpresa = tamanoEmpresa;
        this.ingresos = ingresos;
        this.salarioMin = salarioMin;
        this.salarioMax = salarioMax;
        this.habilidades = habilidades;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSeniority() {
        return seniority;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getSede() {
        return sede;
    }

    public String getIndustria() {
        return industria;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public String getTamanoEmpresa() {
        return tamanoEmpresa;
    }

    public String getIngresos() {
        return ingresos;
    }

    public double getSalarioMin() {
        return salarioMin;
    }

    public double getSalarioMax() {
        return salarioMax;
    }

    public List<String> getHabilidades() {
        return habilidades;
    }

    // Salario promedio de la oferta (si solo hay un valor, min y max son iguales)
    public double getSalarioPromedio() {
        return (salarioMin + salarioMax) / 2;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Cargo=%s | Nivel=%s | Modalidad=%s | Empresa=%s | Industria=%s | Salario=%.2f EUR",
                titulo, seniority, modalidad, empresa, industria, getSalarioPromedio());
    }
}
