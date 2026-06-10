package br.pucrs.alest1.torneio;

import br.pucrs.alest1.colecoes.BinaryTreeOfString;
import br.pucrs.alest1.colecoes.BinaryTreeOfString.Node;
import java.util.ArrayList;
import java.util.List;

public class TorneioEliminatorio {
    private BinaryTreeOfString arvore;

    public TorneioEliminatorio(List<String> participantes) {
        arvore = new BinaryTreeOfString();
        if (participantes.size() < 8 || participantes.size() > 32) {
            throw new IllegalArgumentException("O torneio precisa ter entre 8 e 32 participantes.");
        }
        // Monta a estrutura de árvore de baixo para cima recursivamente
        arvore.addRoot("Final");
        construirArvorePartidas(arvore.getRootNode(), participantes, 0, participantes.size() - 1);
    }

    // Divide a lista para criar a estrutura simétrica das chaves
    private void construirArvorePartidas(Node atual, List<String> part, int inicio, int fim) {
        if (inicio == fim) {
            atual.element = part.get(inicio); // É folha: vira participante
            return;
        }

        atual.element = null; // Nodo interno começa vazio (partida a ocorrer)
        int meio = (inicio + fim) / 2;

        atual.left = new Node(null);
        atual.left.father = atual;
        construirArvorePartidas(atual.left, part, inicio, meio);

        atual.right = new Node(null);
        atual.right.father = atual;
        construirArvorePartidas(atual.right, part, meio + 1, fim);
    }

    public void registrarVencedor(String vencedor) {
        Node nodo = arvore.searchNodeRef(vencedor, arvore.getRootNode());
        if (nodo == null) {
            System.out.println("Jogador não encontrado!");
            return;
        }
        if (nodo.father != null) {
            nodo.father.element = vencedor;
            System.out.println(vencedor + " avançou na chave!");
        } else {
            System.out.println(vencedor + " É O CAMPEÃO DO TORNEIO!");
        }
    }

    // --- ALGORITMO DO LCA (MÉTODO DA DISCIPLINA) ---
    public String obterLCA(String jog1, String jog2) {
        Node n1 = arvore.searchNodeRef(jog1, arvore.getRootNode());
        Node n2 = arvore.searchNodeRef(jog2, arvore.getRootNode());

        if (n1 == null || n2 == null) return "Um ou ambos jogadores não existem.";

        Node lca = encontrarLCAAux(arvore.getRootNode(), n1, n2);
        return lca != null ? (lca.element != null ? lca.element : "Partida Definida por ID Interno") : "Sem ancestral comum";
    }

    private Node encontrarLCAAux(Node raiz, Node n1, Node n2) {
        if (raiz == null || raiz == n1 || raiz == n2) return raiz;

        Node esq = encontrarLCAAux(raiz.left, n1, n2);
        Node dir = encontrarLCAAux(raiz.right, n1, n2);

        if (esq != null && dir != null) return raiz; // Se achou um de cada lado, este nodo é o LCA
        return (esq != null) ? esq : dir;
    }

    public void imprimirDadosEstruturais() {
        System.out.println("Altura da chave: " + arvore.height());
        System.out.println("Número de Partidas (Nodos Internos): " + arvore.countInternalNodes());
        System.out.println("Número de Jogadores (Folhas): " + arvore.countLeaves());
        System.out.println("Percurso Pré-Ordem: " + arvore.positionsPre());
    }
}