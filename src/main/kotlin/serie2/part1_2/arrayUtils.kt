package serie2.part1_2
import kotlin.random.Random
/*
Encontra o menor elemento num max heap
Num max heap, o menor elemento estará obrigatoriamente numa das folhas
Como o heap está representado por um array, as folhas começam no índice heapSize / 2
Esta função procura eficientemente entre as folhas para encontrar o menor valor
*/

fun minimum(maxHeap: Array<Int>, heapSize: Int): Int {

    // Assume que a primeira folha é o mínimo inicialmente
    var min = maxHeap[heapSize / 2]

    // Percorre todas as folhas (desde o índice heapSize / 2 até heapSize - 1)
    for (i in heapSize / 2 until heapSize) {
        // Se o elemento atual for menor que o mínimo encontrado, atualiza o mínimo
        if (maxHeap[i] < min) {
            min = maxHeap[i]
        }
    }
    // Retorna o menor elemento encontrado entre as folhas
    return min

}
