package serie2.part3

class Node<T> (
    var value: T = Any() as T,
    var next: Node<T>? = null,
    var previous: Node<T>? = null) {
}

fun splitEvensAndOdds(list:Node<Int>){
        var current = list.next
        val sentinel = list

        // percorre a lista ate voltar ao nó sentinela
        while (current != sentinel) {
            val nextNode = current!!.next

            // Verifica se o nó atual é par, se sim, move-o para o início da lista
            if (current.value % 2 == 0) {
                // Remover o nó atual da sua posição atuala
                current.previous!!.next = current.next
                current.next!!.previous = current.previous

                // Inserir o nó logo após o sentinela (no início da lista)
                current.next = sentinel.next
                current.previous = sentinel
                sentinel.next!!.previous = current
                sentinel.next = current
            }
            // Avança para o proximo nó
            current = nextNode
        }
}


fun <T> intersection(list1: Node<T>, list2: Node<T>, cmp: Comparator<T>): Node<T>? {
    // Inicializa o início e o final da lista (lista não circular e sem nó sentinela)
    var resultHead: Node<T>? = null
    var resultTail: Node<T>? = null

    // Começa pelos primeiros elementos (ignora sentinelas)
    var current1 = list1.next
    var current2 = list2.next

    // Percorre ambas as listas enquanto nenhuma regressar ao seu nó sentinela
    while (current1 != list1 && current2 != list2) {
        // Compara os valores atuais usando o comparador fornecido
        val comparison = cmp.compare(current1!!.value, current2!!.value)

        if (comparison == 0) {
            // Se os valores são iguais, encontramos um elemento comum
            val commonValue = current1.value

            // Guarda os próximos nós antes de remover os atuais
            var next1 = current1.next
            var next2 = current2.next

            // Remove current1 da list1
            current1.previous!!.next = current1.next
            current1.next!!.previous = current1.previous

            // Remove current2 da list2
            current2.previous!!.next = current2.next
            current2.next!!.previous = current2.previous

            // Reutiliza o nó de current1 e adiciona-o à lista
            current1.previous = resultTail
            current1.next = null

            // Se a lista estiver vazia, inicializa o head e o tail
            if (resultHead == null) {
                resultHead = current1
                resultTail = current1
            } else {
                // Caso contrário, adiciona ao final e atualiza o tail
                resultTail!!.next = current1
                resultTail = current1
            }

            // Salta valores duplicados em list1
            while (next1 != list1 && cmp.compare(next1!!.value, commonValue) == 0) {
                next1 = next1.next
            }

            // Salta valores duplicados em list2
            while (next2 != list2 && cmp.compare(next2!!.value, commonValue) == 0) {
                next2 = next2.next
            }

            // Avança para os próximos elementos distintos em ambas as listas
            current1 = next1
            current2 = next2
        } else if (comparison < 0) {
            // current1 é menor, avança list1
            current1 = current1.next
        } else {
            // current2 é menor, avança list2
            current2 = current2.next
        }
    }

    // Retorna o início da lista resultado (ou null se não houver interseção)
    return resultHead
}

// -------------------- FUNÇÕES DE TESTE / AUXILIARES --------------------

// Função para criar uma lista circular com nó sentinela
fun <T> listaCircularComSentinela(values: List<T>): Node<T> {
    val sentinel = Node<T>()
    sentinel.next = sentinel
    sentinel.previous = sentinel

    // Adiciona os valores à lista circular
    for (i in values) {
        val newNode = Node(i)
        newNode.next = sentinel
        newNode.previous = sentinel.previous
        sentinel.previous!!.next = newNode
        sentinel.previous = newNode
    }
    return sentinel
}

// Imprime os elementos de uma lista circular com sentinela
fun <T> printCircularWithSentinel(sentinel: Node<T>) {
    var current = sentinel.next
    while (current != sentinel) {
        print("${current!!.value} ")
        current = current.next
    }
    println()
}

// Imprime uma lista sem sentinela (e não circular)
fun <T> printLinearList(list: Node<T>?) {
    var current = list
    while (current != null) {
        print("${current.value} ")
        current = current.next
    }
    println()
}


// Função para gerar uma lista de inteiros aleatórios
fun randomIntList(size: Int, max: Int): List<Int> {
    return List(size) { (0..max).random() }
}
// -------------------- FUNÇÃO MAIN --------------------

fun main() {
    println("=== Teste: splitEvensAndOdds ===")

    // Gera uma lista aleatória de inteiros
    val randomList = randomIntList(size = 10, max = 100)
    println("Lista original:")
    println(randomList)

    // Cria a lista circular com sentinela
    val circularList = listaCircularComSentinela(randomList)

    // Aplica splitEvensAndOdds
    splitEvensAndOdds(circularList)

    println("Lista após aplicar splitEvensAndOdds:")
    printCircularWithSentinel(circularList)

    println("\n=== Teste: intersection ===")

    println("Teste de intersection")
    val list1 = listOf(7, 6, 16, 9, 2, 5, 14, 13, 3, 10).sorted()
    val list2 = listOf(8, 5, 6, 17, 12, 9, 14, 13, 18, 11).sorted()

    println("List1 ordenada: $list1")
    println("List2 ordenada: $list2")

    // Cria listas circulares com sentinela
    val circularList1 = listaCircularComSentinela(list1)
    val circularList2 = listaCircularComSentinela(list2)

    // Aplica a função de interseção
    val intersectionList = intersection(circularList1, circularList2, Comparator.naturalOrder())

    println("Resultado da interseção:")
    printLinearList(intersectionList)
}
