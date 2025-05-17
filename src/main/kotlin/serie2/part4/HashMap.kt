package serie2.part4

// Mapa baseado numa tabela de dispersão (hash table)
class HashMap<K, V>(
    initialCapacity: Int = 16,
    val loadFactor: Float = 0.75f,
) : MutableMap<K, V> {

    // Representa um nó na lista ligada de colisões (um par chave-valor)
    private class HashNode<K, V>(
        override val key: K,
        override var value: V,
        var next: HashNode<K, V>? = null,
    ) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            val previous = value
            value = newValue
            return previous
        }
    }

    private var table: Array<HashNode<K, V>?> = arrayOfNulls(initialCapacity)
    override var size = 0
    override val capacity: Int
        get() = table.size

    // gera um indice positivo a partir do hashCode da chave
    private fun hash(key: K): Int {
        var idx = key.hashCode() % capacity
        if (idx < 0) idx += capacity
        return idx
    }

    // retorna o valor associado a chave, ou null se não existir
    override fun get(key: K): V? {
        val index = hash(key)
        var current = table[index]
        while (current != null) {
            if (key == current.key) return current.value
            current = current.next
        }
        return null
    }

    // insere ou atualiza o valor associado a chave
    override fun put(key: K, value: V, ): V? {
        // verifica se a tabela precisa de expansao
        if (size.toFloat() / capacity >= loadFactor) {
            expand()
        }

        val index = hash(key)
        var current = table[index]

        while (current != null) {
            if (key == current.key) {
                val old = current.value
                current.value = value
                return old
            }
            current = current.next
        }

        // Insere um novo nó na tabela
        val newNode = HashNode(key, value, table[index])
        table[index] = newNode
        size++
        return null
    }

    // dobra a capacidade da tabela e redistribui (re-hash) os nós
    private fun expand() {
        val newCapacity = capacity * 2
        val newTable = arrayOfNulls<HashNode<K, V>?>(newCapacity)

        for (i in table.indices) {
            var node = table[i]
            while (node != null) {
                val next = node.next
                var newIndex = node.key.hashCode() % newCapacity
                if (newIndex < 0) newIndex += newCapacity

                // move o nó para a nova tabela
                node.next = newTable[newIndex]
                newTable[newIndex] = node
                node = next
            }
        }

        table = newTable
    }

    // permite iterar sobre os pares chave-valor
    override fun iterator(): Iterator<MutableMap.MutableEntry<K, V>> {
        return object : Iterator<MutableMap.MutableEntry<K, V>> {
            private var tableIndex = 0
            private var currentNode: HashNode<K, V>? = null

            init {
                moveToNext()
            }

            // Move para o próximo nó disponivel na tabela
            private fun moveToNext() {
                if (currentNode?.next != null) {
                    currentNode = currentNode?.next
                    return
                }
                tableIndex++
                while (tableIndex < capacity) {
                    if (table[tableIndex] != null) {
                        currentNode = table[tableIndex]
                        return
                    }
                    tableIndex++
                }
                currentNode = null
            }

            override fun hasNext(): Boolean = currentNode != null

            override fun next(): MutableMap.MutableEntry<K, V> {
                if (!hasNext()) throw NoSuchElementException()
                val current = currentNode!!
                moveToNext()
                return current
            }
        }
    }
}