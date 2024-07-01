package Modelo.Interface;

import Modelo.PPartida;
import Modelo.Partida;
import java.util.List;

public interface CRUD<T> {
    public List listar();
    public int add(PPartida t);
    public int actualizar(PPartida t);
    public void eliminar(int id);
}