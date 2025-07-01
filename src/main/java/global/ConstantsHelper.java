package global;

public class ConstantsHelper {
    public static class AlumnoConstants {
        private static final int estadoRegistrado = 0;
        private static final int estadoMatriculado = 1;
        private static final int estadoRetirado = 2;

        public static int getEstadoRegistrado() {
            return estadoRegistrado;
        }

        public static int getEstadoMatriculado() {
            return estadoMatriculado;
        }

        public static int getEstadoRetirado() {
            return estadoRetirado;
        }
    }
}
