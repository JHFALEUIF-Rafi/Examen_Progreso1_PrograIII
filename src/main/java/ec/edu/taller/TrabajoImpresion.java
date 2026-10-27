package ec.edu.taller;

public class TrabajoImpresion {
    private String codigo;
    private String solicitante;
    private String documento;
    private int prioridad;
    private String estado;

    public TrabajoImpresion(String codigo, String solicitante, String documento, int prioridad) {
        this.codigo = codigo;
        this.solicitante = solicitante;
        this.documento = documento;
        this.prioridad = prioridad;
        this.estado = "PENDIENTE";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {

        this.codigo = codigo;
    }

    public String getSolicitante() {

        return solicitante;
    }

    public void setSolicitante(String solicitante) {

        this.solicitante = solicitante;
    }

    public String getDocumento() {

        return documento;
    }

    public void setDocumento(String documento) {

        this.documento = documento;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + " | " + solicitante + " | " + documento + " | " + prioridad + " | " + estado;
    }
}
