package infrastructure.core.models;

public class Matricula {
    private int correlativo;
    private int codAlumno;
    private int codCurso;
    private String fecha;
    private String hora;

    public Matricula(int correlativo, int codAlumno, int codCurso, String fecha, String hora) {
        this.correlativo = correlativo;
        this.codAlumno = codAlumno;
        this.codCurso = codCurso;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getCorrelativo() {
        return correlativo;
    }

    public int getCodAlumno() {
        return codAlumno;
    }

    public int getCodCurso() {
        return codCurso;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public void setCodAlumno(int codAlumno) {
        this.codAlumno = codAlumno;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
