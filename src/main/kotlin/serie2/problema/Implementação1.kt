package serie2.problema

import java.io.*

// Representa um ponto 2D com coordenadas (x, y)
data class Coord(val x: Float, val y: Float)

// Abre um ficheiro para leitura
fun leitorFicheiro(nome: String): BufferedReader =
    BufferedReader(FileReader(nome))

// Abre um ficheiro para escrita
fun escritorFicheiro(nome: String): PrintWriter =
    PrintWriter(nome)

// Lê dois ficheiros .co e cria um mapa com os pontos e suas origens ("A" ou "B")
fun carregarPontos(f1: String, f2: String): HashMap<Coord, MutableSet<String>> {
    val pontoOrigem = HashMap<Coord, MutableSet<String>>() // Mapeia ponto → origem

    // Lê um ficheiro e associa os pontos à origem indicada
    fun lerFicheiro(caminho: String, origem: String) {
        val reader = leitorFicheiro(caminho)
        reader.useLines { linhas ->
            for (linha in linhas) {
                val limpa = linha.trim()
                if (limpa.startsWith("v")) { // Só processa linhas válidas de ponto
                    val partes = limpa.split(" ").filter { it.isNotEmpty() }
                    if (partes.size >= 4) {
                        val x = partes[2].toFloatOrNull()
                        val y = partes[3].toFloatOrNull()
                        if (x != null && y != null) {
                            val ponto = Coord(x, y)
                            // Adiciona origem ao conjunto associado ao ponto
                            pontoOrigem.computeIfAbsent(ponto) { mutableSetOf() }.add(origem)
                        }
                    }
                }
            }
        }
    }

    // Lê os dois ficheiros
    lerFicheiro(f1, "A")
    lerFicheiro(f2, "B")
    return pontoOrigem
}

// Escreve os pontos num ficheiro, adicionando um ID incremental
fun guardarResultado(nome: String, pontos: Set<Coord>) {
    val writer = escritorFicheiro(nome)
    var id = 1
    for (p in pontos) {
        writer.println("v p$id ${p.x} ${p.y}")
        id++
    }
    writer.close()
}

// Retorna todos os pontos (união)
fun juntar(map: Map<Coord, Set<String>>): Set<Coord> =
    map.keys

// Retorna apenas os pontos que estão em ambos os ficheiros
fun intersecao(map: Map<Coord, Set<String>>): Set<Coord> =
    map.filter { it.value.containsAll(setOf("A", "B")) }.keys

// Retorna os pontos que estão apenas no ficheiro A
fun diferenca(map: Map<Coord, Set<String>>): Set<Coord> =
    map.filter { it.value.contains("A") && !it.value.contains("B") }.keys

// Cria dois ficheiros de teste para a implementação 1
fun criarArquivosTeste1() {
    File("coord1.co").printWriter().use {
        it.println("v A 1.0 1.0")
        it.println("v B 2.0 2.0")
        it.println("v C 3.0 3.0")
    }
    File("coord2.co").printWriter().use {
        it.println("v C 3.0 3.0")
        it.println("v D 4.0 4.0")
        it.println("v E 5.0 5.0")
    }
}

fun main() {
    criarArquivosTeste1()

    println("""
        Bem-vindo ao ProcessPointsCollections (Implementação 1)
        Comandos:
        - carregar <ficheiro1.co> <ficheiro2.co>
        - união <ficheiro_saida.co>
        - intersecão <ficheiro_saida.co>
        - diferenca <ficheiro_saida.co>
        - sair
    """.trimIndent())

    var basePontos: HashMap<Coord, MutableSet<String>>? = null

    while (true) {
        print("> ")
        val input = readLine()?.trim() ?: continue
        val partes = input.split(" ").filter { it.isNotEmpty() }

        when (partes[0].lowercase()) {
            "carregar" -> {
                if (partes.size < 3) {
                    println("Uso: carregar <ficheiro1.co> <ficheiro2.co>")
                    continue
                }
                basePontos = carregarPontos(partes[1], partes[2])
                println("Ficheiros lidos com sucesso.")
            }

            "uniao" -> {
                if (basePontos == null) {
                    println("Use 'carregar' antes.")
                    continue
                }
                if (partes.size < 2) {
                    println("Uso: uniao <ficheiro_saida.co>")
                    continue
                }
                val resultado = juntar(basePontos)
                guardarResultado(partes[1], resultado)
                println("União gravada em '${partes[1]}'.")
            }

            "intersecao" -> {
                if (basePontos == null) {
                    println("Use 'carregar' antes.")
                    continue
                }
                if (partes.size < 2) {
                    println("Uso: intersecao <ficheiro_saida.co>")
                    continue
                }
                val resultado = intersecao(basePontos)
                guardarResultado(partes[1], resultado)
                println("Interseção gravada em '${partes[1]}'.")
            }

            "diferenca" -> {
                if (basePontos == null) {
                    println("Use 'carregar' antes.")
                    continue
                }
                if (partes.size < 2) {
                    println("Uso: diferenca <ficheiro_saida.co>")
                    continue
                }
                val resultado = diferenca(basePontos)
                guardarResultado(partes[1], resultado)
                println("Diferença gravada em '${partes[1]}'.")
            }

            "sair" -> {
                println("Fim do programa.")
                break
            }

            else -> {
                println("Comando inválido. Use: carregar, uniao, intersecao, diferenca, sair")
            }
        }
    }
}
