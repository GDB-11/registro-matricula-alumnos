package infrastructure.core.models;

public class Retiro {
    private int numRetiro;
    private int numMatricula;
    private String fecha;
    private String hora;

    public Retiro(int numRetiro, int numMatricula, String fecha, String hora) {
        this.numRetiro = numRetiro;
        this.numMatricula = numMatricula;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getNumRetiro() {
        return numRetiro;
    }

    public int getNumMatricula() {
        return numMatricula;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setNumRetiro(int numRetiro) {
        this.numRetiro = numRetiro;
    }

    public void setNumMatricula(int numMatricula) {
        this.numMatricula = numMatricula;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
