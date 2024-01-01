val hand1 = Hand(Player.Player1, Direction.left)
val hand2 = Hand(Player.Player1, Direction.right)
val hand3 = Hand(Player.Player2, Direction.left)
val hand4 = Hand(Player.Player2, Direction.right)

var running = true

fun main() {

    var turn = Player.Player1

    val helpMsg = """
            Welcome to Chopstick!
            The variation of the game being played is Suicide (https://www.wikiwand.com/en/Chopsticks_(hand_game)#Variations).
            A hand having a value of -1 signifies that it is dead.
            You can attack, transfer/add, combine, divide, rearrange, exit, print the game or type help for this message.
            Have fun!
            
            
        """.trimIndent()
    println(helpMsg)

    while (running) {
        println("\n--------------------\n")
        printGame(hand1, hand2, hand3, hand4, turn)
        val input = input()
        
        when (input) {
            "attack" -> {
                val attacker = inputHand(turn, "With what hand would you like to attack (left/right)?: ")
                val attacked = inputHand(if (turn == Player.Player1) Player.Player2 else Player.Player1,
                    "Which hand do you want to attack (left/right)?: ")
                attacker.attack(attacked)
            }
            "add", "transfer" -> {
                val adder = inputHand(turn, "With what hand would you like to add? (left/right)?: ")
                adder.add()
            }
            "combine" -> {
                if (turn == Player.Player1)
                    Hand.combine(hand1, hand2)
                else
                    Hand.combine(hand3, hand4)
            }
            "divide" -> {
                val divided = when (turn) {
                    Player.Player1 -> if (hand1.isDead()) hand2
                    else if (hand2.isDead()) hand1
                    else {
                        println("Both your hands are alive!")
                        continue // continue is used to skip all death checks and turn changing after the when expression
                    }
                    Player.Player2 -> if (hand3.isDead()) hand4
                    else if (hand4.isDead()) hand3
                    else {
                        println("Both your hands are alive!")
                        continue
                    }
                }
                try {
                    divided.divide()
                } catch (e: IllegalArgumentException) {
                    println("Your hand needs to have a value greater than 1!")
                    continue
                }
            }
            "rearrange" -> {
                try {
                    if (turn == Player.Player1)
                        Hand.rearrange(hand1, hand2)
                    else
                        Hand.rearrange(hand3, hand4)
                } catch (e: IllegalArgumentException) {
                    println("One of your hands is dead!")
                    continue
                }
            }
            "print" -> {
                printGame(hand1, hand2, hand3, hand4, turn)
                continue
            }
            "exit" -> {
                println("Thanks for playing!")
                running = false
            }
            "help" -> {
                println(helpMsg)
                continue
            }
        }

        hand1.manageValueDeath()
        hand2.manageValueDeath()
        hand3.manageValueDeath()
        hand4.manageValueDeath()

        checkWin(hand1, hand2, hand3, hand4)

        turn = if (turn == Player.Player1) Player.Player2 else Player.Player1
    }
}

fun printGame(hand1: Hand, hand2: Hand, hand3: Hand, hand4: Hand, turn: Player) {
    println(if (turn == Player.Player1)
                "${hand4.value} - ${hand3.value}\t\tPlayer 2\n" +
                        "${hand1.value} - ${hand2.value}\t\tPlayer 1"
            else
                "${hand2.value} - ${hand1.value}\t\tPlayer 1\n" +
                        "${hand3.value} - ${hand4.value}\t\tPlayer 2")
}

fun input(): String {
    var input: String
    do {
        println("What would you like to do?: ")
        input = readln().trim().lowercase()
    } while(input.isBlank())
    return input
}

fun inputHand(player: Player, message: String): Hand {
    do {
        print(message)
        val input = readln().trim().lowercase()
        if (!(input == Direction.left.name || input == Direction.right.name)) {
            println("Please enter left or right.")
        }
        when (input) {
            Direction.left.name -> {
                when (player) {
                    Player.Player1 -> {
                        if (hand1.isDead())
                            println("That hand is dead!")
                        else
                            return hand1
                    }
                    Player.Player2 -> {
                        if (hand3.isDead())
                            println("That hand is dead!")
                        else
                            return hand3
                    }
                }
            }
            Direction.right.name -> {
                when (player) {
                    Player.Player1 -> {
                        if (hand2.isDead())
                            println("That hand is dead!")
                        else
                            return hand2
                    }
                    Player.Player2 -> {
                        if (hand4.isDead())
                            println("That hand is dead!")
                        else
                            return hand4
                    }
                }
            }
        }
    } while (true)
}

fun checkWin(hand1: Hand, hand2: Hand, hand3: Hand, hand4: Hand) {
    if (hand1.isDead() && hand2.isDead()) {
        println("${hand3.player.name} won the game!")
        running = false
    }
    else if (hand3.isDead() && hand4.isDead()) {
        println("${hand1.player.name} won the game!")
        running = false
    }
}