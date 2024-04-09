package equipo.flashcards.linguashake;

public class TarjetaObj {
    private int id;
    private String frase;
    private String respuesta;

    public TarjetaObj(int id, String frase, String respuesta) {
        this.id = id;
        this.frase = frase;
        this.respuesta = respuesta;
    }

    public int getId() {
        return id;
    }

    public String getFrase() {
        return frase;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
