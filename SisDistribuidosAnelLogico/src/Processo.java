
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
        boolean resultadoDeRequisicao = false;
        for (Processo p : AnelLogico.ativos) {
            if (p.isEhCoordenador()) {
                resultadoDeRequisicao = p.recebeRequisicao(this.pid);
            }
        }
        
        //Se n�o haver coordenador, nova elei��o
        if (!resultadoDeRequisicao) {
            this.novaEleicao();
        }
        System.out.println("Requisição finalizada.");
        return resultadoDeRequisicao;
    }

    private boolean recebeRequisicao(int pidOrigemRequisicao) {
        System.out.println("Requisição do processo " + pidOrigemRequisicao + " recebida com sucesso.");
        return true;
    }

    private void novaEleicao() {
        System.out.println("Eleição iniciada.");
        //consulta cada processo, adicionando o id de cada um em uma nova lista

        LinkedList<Integer> idProcessosVerificados = new LinkedList<>();
        for (Processo p : AnelLogico.ativos) {
            p.consultarProcesso(idProcessosVerificados);
        }

        //Percorre a lista em busca do maior ID
        int idNovoCoordenador = this.getPid();
        for (Integer id : idProcessosVerificados) {
            if (id > idNovoCoordenador) {
                idNovoCoordenador = id;
            }
        }
        
        
        boolean resultadoAtualizacao = false;
        resultadoAtualizacao = atualizarCoordenador(idNovoCoordenador);

        if (resultadoAtualizacao) {
            System.out.println("Eleiçã finalizada. Novo coordenador é: " + idNovoCoordenador + ".");
        } else {
            System.out.println("Eleição finalizada. Sem novo coordenador.");
        }
    }

    private void consultarProcesso(LinkedList<Integer> processosConsultados) {
        processosConsultados.add(this.getPid());
    }

    private boolean atualizarCoordenador(int idNovoCoordenador) {
        
    	//Atualiza a propriedade EhCoordenador apenas para o novo eleito
        for (Processo p : AnelLogico.ativos) {
        	p.setEhCoordenador(p.getPid() == idNovoCoordenador);
        }
        return true;
    }
}