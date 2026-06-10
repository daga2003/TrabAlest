package br.pucrs.alest1;

import br.pucrs.alest1.torneio.TorneioEliminatorio;
import br.pucrs.alest1.colecoes.GeneralTreeOfString;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== TRABALHO 3 - ALEST 1 ===");
            System.out.println("[1] Modo 1: Torneio Eliminatório (Árvore Binária)");
            System.out.println("[2] Modo 2: Menu de Aplicativo (Árvore Genérica)");
            System.out.println("[0] Sair do Programa");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                if (opcao == 1) {
                    executarModoTorneio();
                } else if (opcao == 2) {
                    executarModoMenuApp();
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite um número.");
            }
        }
        System.out.println("Programa encerrado com sucesso.");
    }

    // --- INTERFACE DO MODO 1: TORNEIO ---
    private static void executarModoTorneio() {
        System.out.println("\n--- INICIALIZANDO TORNEIO ---");
        System.out.println("Digite o nome de 8 participantes separados por vírgula (ex: J1,J2,J3,J4,J5,J6,J7,J8):");
        String entrada = scanner.nextLine();
        String[] nomes = entrada.split(",");

        List<String> participantes = new ArrayList<>();
        for (String nome : nomes) {
            participantes.add(nome.trim());
        }

        if (participantes.size() < 8 || participantes.size() > 32) {
            System.out.println("Erro: Quantidade inválida de jogadores.");
            return;
        }

        TorneioEliminatorio torneio = new TorneioEliminatorio(participantes);
        System.out.println("Torneio criado com sucesso!");

        int subOpcao = -1;
        while (subOpcao != 0) {
            System.out.println("\n>> MODO TORNEIO <<");
            System.out.println("[1] Registrar Vencedor de Partida");
            System.out.println("[2] Consultas Estruturais (Altura, Folhas, Nós Internos, Percurso)");
            System.out.println("[3] Análise LCA (Próximo confronto possível entre 2 jogadores)");
            System.out.println("[0] Voltar ao Menu Principal");
            System.out.print("Escolha: ");

            try {
                subOpcao = Integer.parseInt(scanner.nextLine());
                if (subOpcao == 1) {
                    System.out.print("Digite o nome do jogador que venceu a rodada atual: ");
                    String vencedor = scanner.nextLine();
                    torneio.registrarVencedor(vencedor);
                } else if (subOpcao == 2) {
                    torneio.imprimirDadosEstruturais();
                } else if (subOpcao == 3) {
                    System.out.print("Jogador 1: ");
                    String j1 = scanner.nextLine();
                    System.out.print("Jogador 2: ");
                    String j2 = scanner.nextLine();
                    System.out.println("LCA (Partida em comum): " + torneio.obterLCA(j1, j2));
                }
            } catch (Exception e) {
                System.out.println("Opção inválida.");
            }
        }
    }

    // --- INTERFACE DO MODO 2: MENU DE APLICATIVO ---
    private static void executarModoMenuApp() {
        GeneralTreeOfString menuTree = new GeneralTreeOfString();
        menuTree.addRoot("App");
        System.out.println("\nMenu de Aplicativo Inicializado com a raiz: 'App'");

        int subOpcao = -1;
        while (subOpcao != 0) {
            System.out.println("\n>> MODO MENU HIERÁRQUICO <<");
            System.out.println("[1] Inserir Novo Item no Menu");
            System.out.println("[2] Mover Subárvore (Re-root Local)");
            System.out.println("[3] Remover Subárvore (Item e descendentes)");
            System.out.println("[4] Ver Visualização em Largura (Estrutura Atual)");
            System.out.println("[5] Consultas (LCA e Caminho de Navegação)");
            System.out.println("[6] Executar Verificador de Consistência (Ciclos)");
            System.out.println("[0] Voltar ao Menu Principal");
            System.out.print("Escolha: ");

            try {
                subOpcao = Integer.parseInt(scanner.nextLine());
                switch (subOpcao) {
                    case 1:
                        System.out.print("Nome do novo item: ");
                        String novo = scanner.nextLine();
                        System.out.print("Nome do item pai existente: ");
                        String pai = scanner.nextLine();
                        if (menuTree.insert(novo, pai)) {
                            System.out.println("Item adicionado!");
                        } else {
                            System.out.println("Erro: Item pai não encontrado.");
                        }
                        break;
                    case 2:
                        System.out.print("Item que deseja mover (com toda sua subárvore): ");
                        String mover = scanner.nextLine();
                        System.out.print("Nome do NOVO item pai: ");
                        String novoPai = scanner.nextLine();
                        if (menuTree.moveSubtree(mover, novoPai)) {
                            System.out.println("Subárvore movida com sucesso!");
                        } else {
                            System.out.println("Operação inválida (Verifique os nomes ou regras de ancestralidade).");
                        }
                        break;
                    case 3:
                        System.out.print("Item que deseja deletar completamente: ");
                        String remover = scanner.nextLine();
                        if (menuTree.removeSubtree(remover)) {
                            System.out.println("Subárvore removida!");
                        } else {
                            System.out.println("Erro ao remover.");
                        }
                        break;
                    case 4:
                        System.out.println("Exibição em Largura (Nível por Nível):");
                        System.out.println(menuTree.positionsWidth());
                        System.out.println("Altura atual do menu: " + menuTree.height());
                        System.out.println("Grau máximo encontrado: " + menuTree.getMaxDegree());
                        break;
                    case 5:
                        System.out.print("Item de Origem (X): ");
                        String x = scanner.nextLine();
                        System.out.print("Item de Destino (Y): ");
                        String y = scanner.nextLine();
                        System.out.println("Menu agrupador comum (LCA): " + menuTree.obterLCA(x, y));
                        System.out.println("Sequência de Navegação (Caminho): " + menuTree.obterCaminho(x, y));
                        break;
                    case 6:
                        if (menuTree.checkConsistency()) {
                            System.out.println("RESULTADO: Árvore consistente! (Raiz única e sem ciclos).");
                        } else {
                            System.out.println("ALERTA: Estrutura corrompida! Ciclo detectado.");
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("Opção inválida.");
            }
        }
    }
}