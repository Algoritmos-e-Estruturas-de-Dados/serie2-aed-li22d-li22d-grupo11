package serie2.part1_2


// Estrutura de dados IntArrayList que implementa uma lista de inteiros com tamanho fixo k
// Opera em disciplina FIFO (First-In, First-Out), como uma fila circular, e suporta a operação addToAll em O(1)

class IntArrayList (private val k: Int) {
    private val dataArray = IntArray(k) // Array de inteiros de tamanho fixo k
    private var start = 0  // Índice do primeiro elemento da lista
    private var end = 0  // Índice do final da lista (proximo elemento a ser adicionado)
    private var size = 0
    private var delta = 0

    // Adiciona um elemento x ao final da lista
    fun append(x: Int): Boolean {
        if (size == k) return false // Lista cheia - não adiciona
        dataArray[end] = x - delta // Armazena o valor ajustado, subtraindo delta (deslocação)
        end = (end + 1) % k  // Avança o índice do final da lista (circular)
        size++  // Incrementa o numero de elementos
        return true
    }

    // Retorna o enésimo elemento da lista (0-based), ou null se for inválido
    fun get(n: Int): Int? {
        if (n < 0 || n >= size) return null  // Verifica se o índice é válido
        val index = (start + n) % k  // Calcula o índice real no array circular
        return dataArray[index] + delta  // Retorna o valor ajustado, somando delta (deslocação)
    }

    // Adiciona x a todos os elementos da lista (em tempo constante)
    fun addToAll(x: Int) {
        delta += x  // Atualiza o deslocamento (delta) para todos os elementos
    }

    // Remove o primeiro elemento da lista (FIFO)
    fun remove(): Boolean {
        if (size == 0) return false  // Lista vazia - não remove
        start = (start + 1) % k  // Avança o índice do início da lista (circular)
        size--  // Decrementa o número de elementos
        return true
    }


    fun iterator(): Iterator<Int> { // Optional
        TODO("Not yet implemented")
    }

}