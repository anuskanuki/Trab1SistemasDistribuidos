
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
                        System.out.println("Processo " + processosAtivos.get(processosAtivos.size() - 1).getPid() + " Criado com sucesso ");
                    }
                    try {
                        Thread.sleep(ADICIONA);
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
                while (true){
           try {
                Thread.sleep(REQUISICAO);
           } catch (Exception e){
           }
           synchronized (lock){
                    if (!processosAtivos.isEmpty()) {
                        int index = new Random().nextInt (processosAtivos.size());
                        Processo p = processosAtivos.get(index);
                        p.enviarRequisicao();
                        System.out.println("Processo " + p.getPid() + " realizou requisição");
                    }
                }
            }
        }
    }).start();
}

    public void inativaProcesso() {
        new Thread (new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(INATIVO_PROCESSO);
                    } catch (Exception e) {
                    }
                    synchronized (lock) {
                        if (!processosAtivos.isEmpty()) {
                            int index = new Random().nextInt(processosAtivos.size());
                            Processo p = processosAtivos.get(index);
                            if (p != null && !p.isEhCoordenador()) {
                                processosAtivos.remove(p);
                                System.out.println("Processo "+ p.getPid() + " esta inativo");
                            }
                        }
                    }
                }
            }
        }).start();

    }

    public void inativaCoordenador() {
        new Thread (new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(INATIVO_COORDENADOR);
                    } catch (Exception e) {
                    }
                    synchronized (lock){
                        Processo coordenador = null;
                        for (Processo p : processosAtivos) {
                            if (p.isEhCoordenador()) {
                                coordenador = p;
                            }
                        }
                        if (coordenador != null) {
                            processosAtivos.remove(coordenador);
                            System.out.println("Coordenador " + coordenador.getPid() + " esta inativo");
                            
                        }
                    }
                }
            }
        }).start();

    }

}
