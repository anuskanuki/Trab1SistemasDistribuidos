
import java.util.LinkedList;

public class Processo {

    private int pid;
    private boolean ehCoordenador;

    public Processo(int pid) {
        setPid(pid);
    }

    public Processo(int pid, boolean ehCoordenador) {
        setPid(pid);
        setEhCoordenador(ehCoordenador);
    }

    public boolean isEhCoordenador() {
        return ehCoordenador;
    }
    public void setEhCoordenador(boolean ehCoordenador) {
        this.ehCoordenador = ehCoordenador;
    }

    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean novaRequisicao() {
        boolean resultadoRdequisicao = false;
        for (Processo p : Anel.processosAtivos) {
            if (p.isEhCoordenador()) {
                resultadoRdequisicao = p.receberRequisicao(this.pid);
            }
        }
        
        //Se não haver coordenador, nova eleição
        if (!resultadoRdequisicao) {
            this.novaEleicao();
        }
        System.out.println("Requisição finalizada");
        return resultadoRdequisicao;
    }

    private boolean receberRequisicao(int pidOrigemRequisicao) {
        System.out.println("Requisicao do processo " + pidOrigemRequisicao + " recebida com sucesso");
        return true;
    }

    private void novaEleicao() {
        System.out.println("Eleição iniciada");
        //consulta cada processo, adicionando o id de cada um em uma nova lista

        LinkedList<Integer> idProcessosConsultados = new LinkedList<>();
        for (Processo p : Anel.processosAtivos) {
            p.consultarProcesso(idProcessosConsultados);
        }

        //Percorre a lista em busca do maior ID
        int idNovoCoordenador = this.getPid();
        for (Integer id : idProcessosConsultados) {
            if (id > idNovoCoordenador) {
                idNovoCoordenador = id;
            }
        }
        
        
        boolean resultadoAtualizacao = false;
        resultadoAtualizacao = atualizarCoordenador(idNovoCoordenador);

        if (resultadoAtualizacao) {
            System.out.println("Eleicao finalizada. Novo coordenador: " + idNovoCoordenador);
        } else {
            System.out.println("Eleicao finalizada. Sem novo coordenador.");
        }
    }

    private void consultarProcesso(LinkedList<Integer> processosConsultados) {
        processosConsultados.add(this.getPid());
    }

    private boolean atualizarCoordenador(int idNovoCoordenador) {
        
    	//Atualiza a propriedade EhCoordenador apenas para o novo eleito
        for (Processo p : Anel.processosAtivos) {
        	p.setEhCoordenador(p.getPid() == idNovoCoordenador);
        }
        return true;
    }
}