package global;

import java.time.LocalDate;

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

    public static class AlumnoCodigo {
        public static int Generar(int ultimoCodigoIngresado) {
            if (ultimoCodigoIngresado == 0) {
                LocalDate now = LocalDate.now();

                int primeraParte = now.getYear() * 100_000;
                int segundaParte = 10_001;

                return primeraParte + segundaParte;
            }

            return ultimoCodigoIngresado + 1;
        }
    }

    public static class MatriculaCodigo {
        public static int Generar(int ultimoCodigoIngresado) {
            if (ultimoCodigoIngresado == 0) {
                return 100_001;
            }

            return ultimoCodigoIngresado + 1;
        }
    }

    public static class RetiroCodigo {
        public static int Generar(int ultimoCodigoIngresado) {
            if (ultimoCodigoIngresado == 0) {
                return 200_001;
            }

            return ultimoCodigoIngresado + 1;
        }
    }
}
