
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
                    	
                        if (processosAtivos.isEmpty()) {
                            processosAtivos.add(new Processo(1, true));
                        } 
                        else {
                            processosAtivos.add(new Processo(processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
                        }
                        
                        System.out.println("Processo " + processosAtivos.get(processosAtivos.size() - 1).getPid() + " Criado com sucesso ");
                    }
                    
                    try {
                        Thread.sleep(TEMPO_NOVO_PROCESSO);
                    } catch (Exception e) { }
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
			                Thread.sleep(TEMPO_NOVAELEICAO);
			           } catch (Exception e){ }
			           
			           synchronized (lock){
			        	   
		                    if (!processosAtivos.isEmpty()) {
		                        int index = new Random().nextInt (processosAtivos.size());
		                        var processo = processosAtivos.get(index);
		                        processo.novaRequisicao();
		                        System.out.println("Processo " + processo.getPid() + " realizou requisição");
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
                        Thread.sleep(TEMPO_INATIVAR_PROCESSO);
                    } catch (Exception e) { }
	                    
	                    
                    synchronized (lock) {
                    	
                        if (!processosAtivos.isEmpty()) {
                            int index = new Random().nextInt(processosAtivos.size());
                            var processo = processosAtivos.get(index);
                            if (processo != null && !processo.isEhCoordenador()) {
                                processosAtivos.remove(processo);
                                System.out.println("Processo "+ processo.getPid() + " esta inativo");
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
                        Thread.sleep(TEMPO_INATIVAR_COORDENADOR);
                    } catch (Exception e) { }
                    
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
