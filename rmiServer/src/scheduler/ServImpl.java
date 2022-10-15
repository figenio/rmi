package scheduler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    protected ServImpl() throws RemoteException {
        // cria notificador
    }

    /* CADASTRO DE CLIENTES
    Recebe nome e ref do cliente
    Retorna chave pública do servidor
    * */
    @Override
    public void registerInterest(String texto, InterfaceCli referenciaCliente) throws RemoteException {
        referenciaCliente.notify(texto);
    }

    /* CADASTRO DE COMPROMISSO
    Cliente informa nome e dados do compromiso
    registra no notificador
    * */

    /* CADASTRO DE ALERTA
    Cliente interessado
    Cliente informa nome e dados do compromiso
    - Os convidados são avisados se querem ou não serem incluídos
    - Se sim, o convidado receberá um alerta antes do compromisso
    * */

    /* CANCELAMENTO DE COMPROMISSO
    * Cliente informa o nome do compromisso a ser cancelado
    * */

    /* CANCELAMENTO DE ALERTA
     * Cliente informa o nome do compromisso a ter o alerta cancelado
     * */

    /* CONSULTA
    * Consulta por dia e retorna todos do dia. */
}
