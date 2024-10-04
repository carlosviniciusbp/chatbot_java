import java.io.*;
import java.util.*;

public class Main {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    static final Map<String, List<Integer>> medicos = new HashMap<>();
    static {
        medicos.put("Cardiologista", Arrays.asList(8, 9, 10, 11, 14, 15, 16));
        medicos.put("Dermatologista", Arrays.asList(9, 10, 11, 14, 15, 16, 17));
        medicos.put("Endocrinologista", Arrays.asList(10, 11, 14, 15, 16, 17, 18));
        medicos.put("Ginecologista", Arrays.asList(7, 8, 9, 10, 11));
        medicos.put("Ortopedista", Arrays.asList(12, 13, 14, 15, 16, 17, 18));
    }

    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, List<String>> agendamentos = new HashMap<>();

    public static void main(String[] args) {
        carregarAgendamentos();
        inicio();
    }

    private static void inicio() {
        System.out.println("Olá! Bem-vindo ao Hospital São Marcelo. Como posso ajudar hoje?");
        System.out.println("1. Agendar Consulta");
        System.out.println("2. Consultar Agendamentos");
        System.out.println("3. Resultados de Exames");
        System.out.println("4. Preço de Exames");
        System.out.println("5. Falar com um Atendente");
        System.out.println("6. Sair");

        int opcao = 0;
        try {
            opcao = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.nextLine();
            inicio();
            return;
        }

        switch (opcao) {
            case 1:
                agendarConsulta();
                break;
            case 2:
                consultarAgendamentos();
                break;
            case 3:
                resultadosDeExames();
                break;
            case 4:
                precoDeExames();
                break;
            case 5:
                falarComAtendente();
                break;
            case 6:
                System.out.println("Obrigado por usar nossos serviços. Até a próxima!");
                scanner.close();
                break;
            default:
                limparTela();
                System.out.println("Opção inválida. Tente novamente.");
                inicio();
                break;
        }
    }

    private static void agendarConsulta() {
        limparTela();
        System.out.println("Por favor, informe o seu CPF.");
        String cpf = "";
        try {
            cpf = scanner.nextLine();
        } catch (Exception e) {
            agendarConsulta();
            return;
        }

        if (!cpf.matches("\\d{11}")) {
            agendarConsulta();
            return;
        }

        System.out.println("Qual especialidade médica você deseja agendar?");
        List<String> especialidades = new ArrayList<>(medicos.keySet());
        for (int i = 0; i < especialidades.size(); i++) {
            System.out.println((i + 1) + ". " + especialidades.get(i));
        }

        int escolhaEspecialidade = 0;
        try {
            escolhaEspecialidade = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.nextLine();
            return;
        }
        String especialidade = especialidades.get(escolhaEspecialidade - 1);

        System.out.println("Por favor, informe a data desejada (formato: dd/MM/yyyy):");
        String data = "";
        try {
            data = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao ler a data. Tente novamente.");
            return;
        }

        System.out.println("Digite o código referente ao horário:");
        List<Integer> horariosDisponiveis = medicos.get(especialidade);
        for (int i = 0; i < horariosDisponiveis.size(); i++) {
            int hora = horariosDisponiveis.get(i);
            for (int j = 0; j < 4; j++) {
                int minutos = j * 15;
                System.out.printf("%d. %02d:%02d\n", (i * 4 + j + 1), hora, minutos);
            }
        }

        int escolhaHorario = 0;
        try {
            escolhaHorario = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.nextLine();
            return;
        }
        int horaEscolhida = horariosDisponiveis.get((escolhaHorario - 1) / 4);
        int minutosEscolhidos = ((escolhaHorario - 1) % 4) * 15;
        String horario = String.format("%02d:%02d", horaEscolhida, minutosEscolhidos);

        String agendamento = "Consulta com " + especialidade + " em " + data + " às " + horario;
        agendamentos.computeIfAbsent(cpf, k -> new ArrayList<>()).add(agendamento);
        salvarAgendamentos();

        System.out.println(ANSI_GREEN + "Sua consulta com " + especialidade + " foi agendada para " + data + " às "
                + horario + "." + ANSI_RESET);

        pressioneEnterParaContinuar();
        inicio();
    }

    private static void precoDeExames() {
        limparTela();
        System.out.println("Por favor, informe o nome do exame que deseja saber o preço.");
        Map<String, Integer> exames = new HashMap<>();
        exames.put("Hemograma", 150);
        exames.put("Raio-X", 200);
        exames.put("Ultrassom", 250);
        exames.put("Ressonância Magnética", 300);
        exames.put("Tomografia Computadorizada", 350);
        exames.put("Eletrocardiograma", 180);
        exames.put("Endoscopia", 220);
        exames.put("Colonoscopia", 270);
        exames.put("Mamografia", 190);
        exames.put("Papanicolau", 130);
        exames.put("Teste de Esforço", 210);
        exames.put("Ecocardiograma", 240);
        exames.put("Densitometria Óssea", 260);
        exames.put("Exame de Urina", 120);
        exames.put("Exame de Fezes", 110);

        for (String key : exames.keySet()) {
            System.out.println(key);
        }

        String exame = scanner.nextLine();
        boolean encontrado = false;

        for (String key : exames.keySet()) {
            if (key.toLowerCase().matches(".*" + exame.toLowerCase() + ".*")) {
                limparTela();
                System.out.println("O preço do exame " + key + " é R$ " + exames.get(key) + ",00.");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Desculpe, não fazemos esse exame na nossa unidade.");
        }

        pressioneEnterParaContinuar();
        inicio();
    }

    private static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void iniciarTimer(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void pressioneEnterParaContinuar() {
        System.out.println("\n\nPressione Enter para continuar...");
        scanner.nextLine();
        limparTela();
    }

    private static void resultadosDeExames() {
        limparTela();
        System.out.println("Por favor, informe o seu CPF e a data de nascimento para verificar seus exames.");
        @SuppressWarnings("unused")
        String cpf = scanner.nextLine();
        @SuppressWarnings("unused")
        String dataNascimento = scanner.nextLine();

        limparTela();

        iniciarTimer(1);

        System.out.println("Verificando seus exames...");

        System.out.println(
                "No momento, não há resultados disponíveis. Tente novamente mais tarde ou entre em contato com o hospital.\n\n");
        pressioneEnterParaContinuar();
        inicio();
    }

    private static void consultarAgendamentos() {
        limparTela();
        System.out.println("Por favor, informe o seu CPF.");
        String cpf = scanner.nextLine();

        List<String> agendamentosUsuario = agendamentos.get(cpf);
        if (agendamentosUsuario == null || agendamentosUsuario.isEmpty()) {
            System.out.println("Você não tem agendamentos futuros.");
        } else {
            System.out.println("Estes são seus agendamentos futuros:");
            for (String agendamento : agendamentosUsuario) {
                System.out.println(ANSI_GREEN + agendamento + ANSI_RESET);
            }

            System.out.println("\n");

            System.out.println("Deseja cancelar ou reagendar algum?");
            System.out.println("1. Cancelar Consulta");
            System.out.println("2. Reagendar Consulta");
            System.out.println("3. Voltar ao Menu Principal");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cancelarConsulta(cpf);
                    break;
                case 2:
                    reagendarConsulta(cpf);
                    break;
                case 3:
                    inicio();
                    break;
                default:
                    limparTela();
                    System.out.println("Opção inválida. Tente novamente.");
                    consultarAgendamentos();
                    break;
            }
        }
    }

    private static void cancelarConsulta(String cpf) {
        limparTela();
        System.out.println("Informe o número da consulta que deseja cancelar:");
        int numeroConsulta = scanner.nextInt();
        scanner.nextLine();

        List<String> agendamentosUsuario = agendamentos.get(cpf);
        if (agendamentosUsuario != null && numeroConsulta > 0 && numeroConsulta <= agendamentosUsuario.size()) {
            agendamentosUsuario.remove(numeroConsulta - 1);
            salvarAgendamentos();
            System.out.println("Consulta cancelada com sucesso.");
        } else {
            System.out.println("Número de consulta inválido.");
        }
        pressioneEnterParaContinuar();
        inicio();
    }

    private static void reagendarConsulta(String cpf) {
        limparTela();
        System.out.println("Informe o número da consulta que deseja reagendar:");
        int numeroConsulta = scanner.nextInt();
        scanner.nextLine();

        List<String> agendamentosUsuario = agendamentos.get(cpf);
        if (agendamentosUsuario != null && numeroConsulta > 0 && numeroConsulta <= agendamentosUsuario.size()) {
            System.out.println("Escolha a nova data e horário:");
            String novoDataHorario = scanner.nextLine();

            String agendamento = agendamentosUsuario.get(numeroConsulta - 1);
            String especialidade = agendamento.split(" ")[2];
            agendamentosUsuario.set(numeroConsulta - 1, "Consulta com " + especialidade + " em " + novoDataHorario);
            salvarAgendamentos();
            System.out.println("Consulta reagendada com sucesso.");
        } else {
            System.out.println("Número de consulta inválido.");
        }
        pressioneEnterParaContinuar();
        inicio();
    }

    private static void falarComAtendente() {
        limparTela();
        System.out.println("Vou transferir você para um de nossos atendentes. Por favor, aguarde um momento.");

        inicio();
    }

    private static void carregarAgendamentos() {

        try (BufferedReader reader = new BufferedReader(new FileReader("agendamentos.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";", 2);
                String cpf = partes[0];
                String agendamento = partes[1];
                agendamentos.computeIfAbsent(cpf, k -> new ArrayList<>()).add(agendamento);
            }
        } catch (IOException e) {
            File file = new File("agendamentos.txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    private static void salvarAgendamentos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("agendamentos.txt"))) {
            for (Map.Entry<String, List<String>> entry : agendamentos.entrySet()) {
                String cpf = entry.getKey();
                for (String agendamento : entry.getValue()) {
                    writer.write(cpf + ";" + agendamento);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar agendamentos: " + e.getMessage());
        }
    }
}