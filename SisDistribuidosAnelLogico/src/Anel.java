
import java.util.ArrayList;
import java.util.Random;

public class Anel {

    private final int ADICIONA = 3000;
    private final int REQUISICAO = 2500;
    private final int INATIVO_COORDENADOR = 10000;
    private final int INATIVO_PROCESSO = 8000;

    public static ArrayList<Processo> processosAtivos;
    private final Object lock = new Object();

    public Anel() {
        processosAtivos = new ArrayList<Processo>();
    }

    
}
