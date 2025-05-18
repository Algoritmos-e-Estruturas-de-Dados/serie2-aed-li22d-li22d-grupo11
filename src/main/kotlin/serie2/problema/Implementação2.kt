package serie2.problema

import java.io.*  // Importa classes necessárias para leitura e escrita de ficheiros
import serie2.part4.HashMap  // Usa a HashMap personalizada do teu projeto

// Representa um ponto 2D com coordenadas x e y (sem ID, pois o ID é gerado na escrita)
data class Coord2D(val x: Float, val y: Float)

// Lê dois ficheiros e associa os pontos à sua origem ("A" ou "B")
fun lerColetados(fich1: String, fich2: String): HashMap<Coord2D, MutableSet<String>> {
    val tabela = HashMap<Coord2D, MutableSet<String>>() // Mapa ponto → conjunto de origens

    // Função auxiliar que processa cada ficheiro
    fun extrair(caminho: String, marcador: String) {
        val reader = File(caminho).bufferedReader()
        reader.useLines { linhas ->
            for (linha in linhas) {
                val limpa = linha.trim()
                if (limpa.startsWith("v")) {  // Apenas processa linhas de pontos
                    val dados = linha.split(" ").filter { it.isNotEmpty() }
                    if (dados.size >= 4) {
                        val x = dados[2].toFloatOrNull()
                        val y = dados[3].toFloatOrNull()
                        if (x != null && y != null) {
                            val ponto = Coord2D(x, y)
                            // Adiciona o marcador ("A" ou "B") ao conjunto de origens
                            val origem = tabela.get(ponto) ?: mutableSetOf<String>().also { tabela.put(ponto, it) }
                            origem.add(marcador)
                        }
                    }
                }
            }
        }
    }

    // Lê os dois ficheiros fornecidos
    extrair(fich1, "A")
    extrair(fich2, "B")
    return tabela
}

// Escreve os pontos resultantes num ficheiro com o formato "v id x y"
fun escreverResultado(nomeSaida: String, dados: Set<Coord2D>) {
    val writer = File(nomeSaida).printWriter()
    var id = 1
    for (coord in dados) {
        writer.println("v p$id ${coord.x} ${coord.y}")
        id++
    }
    writer.close()
}

// Retorna todos os pontos encontrados (união)
fun unirDados(estrutura: HashMap<Coord2D, MutableSet<String>>): Set<Coord2D> {
    val resultado = mutableSetOf<Coord2D>()
    for (registo in estrutura) {
        resultado.add(registo.key)
    }
    return resultado
}

// Retorna apenas os pontos que estão em ambas as origens ("A" e "B")
fun cruzarDados(estrutura: HashMap<Coord2D, MutableSet<String>>): Set<Coord2D> {
    val resultado = mutableSetOf<Coord2D>()
    for (registo in estrutura) {
        if (registo.value.containsAll(setOf("A", "B"))) {
            resultado.add(registo.key)
        }
    }
    return resultado
}

// Retorna os pontos que aparecem só na origem "A"
fun subtrairDados(estrutura: HashMap<Coord2D, MutableSet<String>>): Set<Coord2D> {
    val resultado = mutableSetOf<Coord2D>()
    for (registo in estrutura) {
        val fontes = registo.value
        if ("A" in fontes && "B" !in fontes) {
            resultado.add(registo.key)
        }
    }
    return resultado
}

// Cria dois ficheiros de teste para a implementação 2
fun criarArquivosTeste2() {
    File("coordA.co").printWriter().use {
        it.println("v A 1.0 1.0")
        it.println("v B 2.0 2.0")
        it.println("v C 3.0 3.0")
    }
    File("coordB.co").printWriter().use {
        it.println("v C 3.0 3.0")
        it.println("v D 4.0 4.0")
        it.println("v E 5.0 5.0")
    }
}

fun main() {
    criarArquivosTeste2()

    println("""
        Bem-vindo ao ProcessPointsCollections (Implementação 2)
        Comandos disponíveis:
        - importar <ficheiro1.co> <ficheiro2.co>
        - unir <ficheiro_saida.co>
        - cruzar <ficheiro_saida.co>
        - subtrair <ficheiro_saida.co>
        - sair
    """.trimIndent())

    var pontos: HashMap<Coord2D, MutableSet<String>>? = null

    while (true) {
        print("> ")
        val entrada = readlnOrNull() ?: break
        val termos = entrada.trim().split(' ').filter { it.isNotEmpty() }

        when (termos[0].lowercase()) {
            "importar" -> {
                if (termos.size < 3) {
                    println("Uso: importar <ficheiro1> <ficheiro2>")
                    continue
                }
                try {
                    pontos = lerColetados(termos[1], termos[2])
                    println("Dados lidos com sucesso.")
                } catch (e: Exception) {
                    println("Erro na leitura: ${e.message}")
                }
            }

            "unir" -> {
                if (pontos == null) {
                    println("Importe os ficheiros antes com 'importar'")
                    continue
                }
                if (termos.size < 2) {
                    println("Uso: unir <ficheiro_saida>")
                    continue
                }
                val output = termos[1]
                val result = unirDados(pontos)
                escreverResultado(output, result)
                println("União concluída → $output")
            }

            "cruzar" -> {
                if (pontos == null) {
                    println("Importe os ficheiros antes com 'importar'")
                    continue
                }
                if (termos.size < 2) {
                    println("Uso: cruzar <ficheiro_saida>")
                    continue
                }
                val output = termos[1]
                val result = cruzarDados(pontos)
                escreverResultado(output, result)
                println("Interseção concluída → $output")
            }

            "subtrair" -> {
                if (pontos == null) {
                    println("Importe os ficheiros antes com 'importar'")
                    continue
                }
                if (termos.size < 2) {
                    println("Uso: subtrair <ficheiro_saida>")
                    continue
                }
                val output = termos[1]
                val result = subtrairDados(pontos)
                escreverResultado(output, result)
                println("Diferença concluída → $output")
            }

            "sair" -> {
                println("Encerrando aplicação.")
                break
            }

            else -> {
                println("Comando não reconhecido. Use: importar, unir, cruzar, subtrair, sair")
            }
        }
    }
}
