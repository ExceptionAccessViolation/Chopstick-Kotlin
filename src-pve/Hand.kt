class Hand (val player: Player, val direction: Direction) {
    companion object {
        // static as it's better to have it static when we don't know which hand will call it
        fun combine(hand1: Hand, hand2: Hand) {
            require(!hand1.isDead() && !hand2.isDead())
            hand1.value += hand2.value
            println("${hand1.player.name} combined hands, resulting in the ${hand1.direction.name} " +
                    "hand's value being ${hand1.value}")
            hand2.kill()
        }

        fun rearrange(hand1: Hand, hand2: Hand) {
            require(!hand1.isDead() && !hand2.isDead())
            val valuesArray = intArrayOf(hand1.value, hand2.value)
            valuesArray.sortDescending()
            when {
                valuesArray.contentEquals(intArrayOf(4, 1)) -> {
                    hand1.value = 3
                    hand2.value = 2
                }
                valuesArray.contentEquals(intArrayOf(3, 2)) -> {
                    hand1.value = 4
                    hand2.value = 1
                }
                valuesArray.contentEquals(intArrayOf(3, 3)) -> {
                    hand1.value = 4
                    hand2.value = 2
                }
                valuesArray.contentEquals(intArrayOf(4, 2)) -> {
                    hand1.value = 3
                    hand2.value = 3
                }
                valuesArray.contentEquals(intArrayOf(2, 2)) -> {
                    hand1.value = 3
                    hand2.value = 1
                }
                valuesArray.contentEquals(intArrayOf(3, 1)) -> {
                    hand1.value = 2
                    hand2.value = 2
                }
            }
            println("${hand1.player.name} rearranged their hands, resulting " +
                    "in the hands having a value of ${hand1.value} and ${hand2.value}")
        }
    }

    var value: Int = 1

    fun attack(attacked: Hand) {
        attacked.value += this.value
        println("${this.player.name}'s ${this.direction.name} hand attacked ${attacked.player.name}'s ${attacked.direction.name} hand, " +
                "resulting in its value being ${attacked.value}!")
    }

    fun add() { // same as transfer
        val otherHand = this.otherHand()
        otherHand.value += this.value
        println("${this.player.name}'s ${this.direction.name} hand added its value to ${otherHand.player.name}'s " +
                "${otherHand.direction.name} hand, resulting in its value being ${otherHand.value}")
    }

    fun divide() {
        when {
            this.value <= 1 -> throw IllegalArgumentException()
            this.value == 3 -> {
                this.value = 2
                this.otherHand().value = 1
            }
            this.value % 2 == 0 -> {
                this.value /= 2
                this.otherHand().value = this.value
            }
        }
        println("${this.player.name} divided their ${this.direction.name} hand, " +
                "resulting in the hands having a value of ${this.value} and ${this.otherHand().value}")
    }

    fun manageValueDeath() {
        if (this.value > 5)
            this.value -= 5
        if (this.value == 5)
            this.kill()
    }

    private fun kill() {
        this.value = -1
        println("${this.player.name}'s ${this.direction.name} hand died!")
    }

    fun isDead(): Boolean = this.value == -1

    private fun otherHand(): Hand { // return the other hand of the respective player
        return when (this.player) {
            Player.Player -> if (this.direction == Direction.left) hand2 else hand1
            Player.Computer -> if (this.direction == Direction.left) hand4 else hand3
        }
    }
}