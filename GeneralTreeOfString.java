package br.pucrs.alest1.colecoes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GeneralTreeOfString {

    public static final class Node {
        public String element;
        public Node father;
        public List<Node> children;

        public Node(String element) {
            this.element = element;
            this.father = null;
            this.children = new ArrayList<>();
        }
    }

    private Node root;

    public GeneralTreeOfString() {
        this.root = null;
    }

    public Node getRoot() { return root; }

    public boolean addRoot(String element) {
        if (root != null) return false;
        root = new Node(element);
        return true;
    }

    // Busca em profundidade para achar qualquer item do menu pelo nome
    public Node searchNodeRef(String element, Node target) {
        if (target == null) return null;
        if (target.element.equals(element)) return target;

        for (Node child : target.children) {
            Node res = searchNodeRef(element, child);
            if (res != null) return res;
        }
        return null;
    }

    // Funcionalidade 2: Inserir item
    public boolean insert(String novoItem, String paiItem) {
        Node pai = searchNodeRef(paiItem, root);
        if (pai == null) return false;

        Node novo = new Node(novoItem);
        novo.father = pai;
        pai.children.add(novo);
        return true;
    }

    // Funcionalidade 3: Mover Subárvore (Re-root local)
    public boolean moveSubtree(String itemParaMover, String novoPaiItem) {
        Node nodoMover = searchNodeRef(itemParaMover, root);
        Node novoPai = searchNodeRef(novoPaiItem, root);

        if (nodoMover == null || novoPai == null || nodoMover == root) return false;

        // Evita mover um nó para dentro de um filho dele mesmo (geraria ciclo ou órfãos)
        if (isAncestor(nodoMover, novoPai)) {
            System.out.println("Erro: Não é possível mover um menu para dentro de um submenu dele mesmo!");
            return false;
        }

        // 1. Desconecta do pai antigo
        if (nodoMover.father != null) {
            nodoMover.father.children.remove(nodoMover);
        }

        // 2. Conecta no novo pai
        nodoMover.father = novoPai;
        novoPai.children.add(nodoMover);
        return true;
    }

    // Funcionalidade 4: Remover subárvore inteira
    public boolean removeSubtree(String item) {
        Node nodo = searchNodeRef(item, root);
        if (nodo == null || nodo == root) return false; // Não pode remover a raiz do app por aqui

        if (nodo.father != null) {
            nodo.father.children.remove(nodo); // O Garbage Collector do Java limpa os filhos automaticamente
        }
        return true;
    }

    // --- MÉTODOS DE CONSULTA EXIGIDOS ---

    public int height() { return heightAux(root); }
    private int heightAux(Node n) {
        if (n == null || n.children.isEmpty()) return 0;
        int maxH = -1;
        for (Node child : n.children) {
            maxH = Math.max(maxH, heightAux(child));
        }
        return 1 + maxH;
    }

    public int getMaxDegree() { return getMaxDegreeAux(root); }
    private int getMaxDegreeAux(Node n) {
        if (n == null) return 0;
        int maxDegree = n.children.size();
        for (Node child : n.children) {
            maxDegree = Math.max(maxDegree, getMaxDegreeAux(child));
        }
        return maxDegree;
    }

    // --- VERIFICADOR DE CONSISTÊNCIA (Funcionalidade 6) ---
    // Checa se a árvore tem ciclos e se a raiz é única
    public boolean checkConsistency() {
        if (root == null) return false;

        List<Node> visitados = new ArrayList<>();
        Queue<Node> fila = new LinkedList<>();

        fila.add(root);
        while (!isEmptyQueue(fila)) {
            Node atual = fila.poll();

            if (visitados.contains(atual)) {
                return false; // Ciclo detectado! O mesmo nodo foi acessado por dois caminhos
            }
            visitados.add(atual);

            for (Node child : atual.children) {
                fila.add(child);
            }
        }
        return true; // Árvore limpa e consistente
    }

    // --- PERCURSO EM LARGURA ---
    public List<String> positionsWidth() {
        List<String> lista = new ArrayList<>();
        if (root == null) return lista;

        Queue<Node> fila = new LinkedList<>();
        fila.add(root);
        while (!isEmptyQueue(fila)) {
            Node atual = fila.poll();
            lista.add(atual.element);
            for (Node child : atual.children) {
                fila.add(child);
            }
        }
        return lista;
    }

    // --- AUXILIARES ---
    private boolean isAncestor(Node possivelAncestral, Node nodo) {
        Node atual = nodo;
        while (atual != null) {
            if (atual == possivelAncestral) return true;
            atual = atual.father;
        }
        return false;
    }

    private boolean isEmptyQueue(Queue<?> q) { return q.isEmpty(); }

    // --- CONTINUAÇÃO DA CLASSE GeneralTreeOfString ---

    // Funcionalidade 5: LCA (Lowest Common Ancestor) para Árvore Genérica
    public String obterLCA(String item1, String item2) {
        Node n1 = searchNodeRef(item1, root);
        Node n2 = searchNodeRef(item2, root);

        if (n1 == null || n2 == null) return "Um ou ambos os itens não existem.";

        // Coleta todos os ancestrais de n1 subindo até a raiz
        List<Node> ancestraisN1 = new ArrayList<>();
        Node atual = n1;
        while (atual != null) {
            ancestraisN1.add(atual);
            atual = atual.father;
        }

        // Sobe a partir de n2; o primeiro ancestral de n2 que também estiver na lista de n1 é o LCA
        atual = n2;
        while (atual != null) {
            if (ancestraisN1.contains(atual)) {
                return atual.element; // Achamos o agrupador comum mais baixo
            }
            atual = atual.father;
        }
        return null;
    }

    // Funcionalidade 5: Caminho (sequência de navegação de X até Y)
    public List<String> obterCaminho(String deItem, String paraItem) {
        Node inicio = searchNodeRef(deItem, root);
        Node fim = searchNodeRef(paraItem, root);

        List<String> caminho = new ArrayList<>();
        if (inicio == null || fim == null) return caminho; // Caminho vazio se algum não existir

        // Para achar o caminho em árvores genéricas, a estratégia mais fácil
        // é achar o LCA entre eles, subir de X até o LCA, e depois descer do LCA até Y.
        String lcaNome = obterLCA(deItem, paraItem);
        Node lcaNode = searchNodeRef(lcaNome, root);

        // 1. Sobe do início até o LCA
        Node atual = inicio;
        while (atual != lcaNode && atual != null) {
            caminho.add(atual.element);
            atual = atual.father;
        }
        caminho.add(lcaNode.element); // Adiciona o ponto de encontro

        // 2. Coleta o caminho do LCA até o fim (invertido)
        List<String> descida = new ArrayList<>();
        atual = fim;
        while (atual != lcaNode && atual != null) {
            descida.add(0, atual.element); // Insere sempre no início para manter a ordem correta de descida
            atual = atual.father;
        }

        caminho.addAll(descida);
        return caminho;
    }
}