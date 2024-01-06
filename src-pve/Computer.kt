import kotlin.random.Random

class Computer (val hand1: Hand, val hand2: Hand, val left: Hand, val right: Hand) {
//    val commands = listOf("attack", "transfer", "combine", "divide", "rearrange")
    private var commands = mutableListOf("")

    private fun updateCommands(commands: MutableList<String>): MutableList<String> {
        commands.clear()
        commands.add("attack")
        when {
            !left.isDead() && !right.isDead() -> {
                commands.addAll(listOf("transfer", "combine"))
                if (left.value + right.value > 3)
                    commands.add("rearrange")
            }
            left.isDead() || right.isDead() -> {
                val aliveHand = if (left.isDead()) right else left
                if (aliveHand.value > 1)
                    commands.add("divide")
            }
        }
        return commands
    }

    fun action() {
        commands = updateCommands(commands)
        val command = commands[Random.nextInt(commands.size)]
        when (command) {
            "attack" -> {
                when {
                    !left.isDead() && !right.isDead() -> {
                        while (true) {
                            val attackerRandom = Random.nextInt(2)
                            val attacker = if (attackerRandom == 0) left else right
                            val attackedRandom = Random.nextInt(2)
                            val attacked = if (attackedRandom == 0) hand1 else hand2
                            if (attacked.isDead())
                                continue
                            attacker.attack(attacked)
                            break
                        }
                    }
                    left.isDead() -> {
                        if (!hand1.isDead() && !hand2.isDead()) {
                            val random = Random.nextInt(2)
                            right.attack(if (random == 0) hand1 else hand2)
                        }
                        if (hand1.isDead())
                            right.attack(hand2)
                        if (hand2.isDead())
                            right.attack(hand1)
                    }
                    right.isDead() -> {
                        if (!hand1.isDead() && !hand2.isDead()) {
                            val random = Random.nextInt(2)
                            if (random == 0)
                                left.attack(hand1)
                            else
                                left.attack(hand2)
                        }
                        if (hand1.isDead())
                            left.attack(hand2)
                        if (hand2.isDead())
                            left.attack(hand1)
                    }
                }
            }
            "transfer" -> {
                val addingHand = if (Random.nextInt(2) == 0) left else right
                addingHand.add()
            }
            "combine" -> Hand.combine(left, right)
            "divide" -> {
                val aliveHand = if (left.isDead()) right else left
                aliveHand.divide()
            }
            "rearrange" -> Hand.rearrange(left, right)
        }
    }
}