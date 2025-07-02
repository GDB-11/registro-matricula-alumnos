package infrastructure.core.interfaces;

import global.Result;
import infrastructure.core.models.Retiro;

import java.util.List;

public interface IRetiroRepository {
    Result<List<Retiro>> getAllRetiros();
    Result<Retiro> saveRetiro(Retiro retiro);
    Result<Retiro> getRetiroByCodigo(int numRetiro);
    Result<Void> deleteRetiro(int numRetiro);
    Result<Integer> getUltimoNumRetiroIngresado();
}
