
import java.util.ArrayList;
import java.util.Random;

public class MainClass {

    public static void main(String[] args) {

        AnelLogico executer = new AnelLogico();
        executer.criaProcessos();
        executer.fazRequisicoes();
        executer.inativaCoordenador();
        executer.inativaProcesso();
    }
}

public class AnelLogico {

    private final int TEMPO_NOVO_PROCESSO = 3000;
    private final int TEMPO_NOVAELEICAO = 2500;
    private final int TEMPO_INATIVAR_COORDENADOR = 10000;
    private final int TEMPO_INATIVAR_PROCESSO = 8000;

    public static ArrayList<Processo> ativos;
    private final Object lock = new Object();

    public AnelLogico() {
        ativos = new ArrayList<Processo>();
    }

    public void criaProcessos() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {

                        if (ativos.isEmpty()) {
                            ativos.add(new Processo(1, true));
                        } else {
                            ativos.add(new Processo(ativos.get(ativos.size() - 1).getPid() + 1, false));
                        }

                        System.out.println("Processo " + ativos.get(ativos.size() - 1).getPid() + " criado com sucesso.");
                    }

                    try {
                        Thread.sleep(TEMPO_NOVO_PROCESSO);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void fazRequisicoes() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(TEMPO_NOVAELEICAO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    synchronized (lock) {

                        if (!ativos.isEmpty()) {
                            int index = new Random().nextInt(ativos.size());
                            Processo processoRequisicao = ativos.get(index);
                            processoRequisicao.novaRequisicao();
                            System.out.println("Processo " + processoRequisicao.getPid() + " realizou requisição.");
                        }
                    }
                }
            }
        }).start();
    }

    public void inativaProcesso() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(TEMPO_INATIVAR_PROCESSO);
                    } catch (Exception e) {
                    }

                    synchronized (lock) {

                        if (!ativos.isEmpty()) {
                            int index = new Random().nextInt(ativos.size());
                            Processo processoDeRemover = ativos.get(index);
                            if (processoDeRemover != null && !processoDeRemover.isEhCoordenador()) {
                                ativos.remove(processoDeRemover);
                                System.out.println("O processo " + processoDeRemover.getPid() + " está inativo.");
                            }
                        }
                    }
                }
            }
        }).start();

    }

    public void inativaCoordenador() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(TEMPO_INATIVAR_COORDENADOR);
                    } catch (Exception e) {
                    }

                    synchronized (lock) {

                        Processo coordenador = null;
                        for (Processo p : ativos) {
                            if (p.isEhCoordenador()) {
                                coordenador = p;
                            }
                        }

                        if (coordenador != null) {
                            ativos.remove(coordenador);
                            System.out.println("O coordenador " + coordenador.getPid() + " está inativo.");
                        }
                    }
                }
            }
        }).start();

    }
}
