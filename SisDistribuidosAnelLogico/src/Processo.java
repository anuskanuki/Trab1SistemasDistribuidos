
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

    public boolean enviarRequisicao() {
        boolean resultadoRdequisicao = false;
        for (Processo p : Anel.processosAtivos) {
            if (p.isEhCoordenador()) {
                resultadoRdequisicao = p.receberRequisicao(this.pid);
            }
        }
        //se n tem coordenador:
        if (!resultadoRdequisicao) {
            this.realizarEleicao();
        }
        System.out.println("Fim da requisicao");
        return resultadoRdequisicao;
    }

    private boolean receberRequisicao(int pidOrigemRequisicao) {
        System.out.println("Requisicao do processo " + pidOrigemRequisicao + " recebida com sucesso");
        return true;
    }

    private void realizarEleicao() {
        System.out.println("Processo de eleicao iniciado");
        //consulta cada processo, adicionando o id de cada um em uma nova lista

        LinkedList<Integer> idProcessosConsultados = new LinkedList<>();
        for (Processo p : Anel.processosAtivos) {
            p.consultarProcesso(idProcessosConsultados);
        }

        //dps percorre a lista em busca do maior ID
        int idNovoCoordenador = this.getPid();
        for (Integer id : idProcessosConsultados) {
            if (id > idNovoCoordenador) {
                idNovoCoordenador = id;
            }
        }
        //dps atualiza o novo coordenador:
        boolean resultadoAtualizacao = false;
        resultadoAtualizacao = atualizarCoordenador(idNovoCoordenador);

        if (resultadoAtualizacao) {
            System.out.println("Eleicao feita com sucesso. Novo coordenador: " + idNovoCoordenador);
        } else {
            System.out.println("A eleicao falhou. Não foi encontrado um novo coordenador.");
        }
    }

    private void consultarProcesso(LinkedList<Integer> processosConsultados) {
        processosConsultados.add(this.getPid());
    }

    private boolean atualizarCoordenador(int idNovoCoordenador) {
        //garante que nao exista nenhum outro processo cadastrado como coordenador a não ser o novo eleito
        for (Processo p : Anel.processosAtivos) {
            if (p.getPid() == idNovoCoordenador) {
                p.setEhCoordenador(true);
            } else {
                p.setEhCoordenador(false);
            }
        }
        return true;
    }

}
