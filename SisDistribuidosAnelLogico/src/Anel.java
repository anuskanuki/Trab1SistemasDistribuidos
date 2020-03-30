
import java.util.ArrayList;

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

    public void criaProcessos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        if (processosAtivos.isEmpty()) {
                            processosAtivos.add(new Processo(1, true));
                        } else {
                            processosAtivos.add(new Processo(processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
                        }
                        System.out.println("Processo " + processosAtivos.get(processosAtivos.size() - 1).getPid() + 1);
                    }
                    try {
                        Thread.sleep(ADICIONA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void fazRequisicoes() {

    }

    public void inativaProcesso() {

    }

    public void inativaCoordenador() {

    }

}
