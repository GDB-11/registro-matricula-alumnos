package application.core.interfaces;

import global.Result;
import infrastructure.core.models.Retiro;

import java.util.List;

public interface IRetiro {
    Result<List<Retiro>> getAllRetiros();
    Result<Retiro> saveRetiro(int numMatricula);
    Result<Void> deleteRetiro(int numRetiro);
}
