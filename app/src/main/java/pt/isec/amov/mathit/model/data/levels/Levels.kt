package pt.isec.amov.mathit.model.data.levels

enum class Levels {
    //OLA
    LEVEL1{
        override var operations : ArrayList<String> = arrayListOf<String>("+")
        override var maxNumb = 10
        override fun toString(): String {
            return "1"
        }
    },
    LEVEL2{
        override var operations : ArrayList<String> = arrayListOf<String>("+")
        override var maxNumb = 20
        override fun toString(): String {
            return "2"
        }
    },
    LEVEL3{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-")
        override var maxNumb = 20
        override fun toString(): String {
            return "3"
        }
    },
    LEVEL4{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-")
        override var maxNumb = 40
        override fun toString(): String {
            return "4"
        }
    },
    LEVEL5{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*")
        override var maxNumb = 40
        override fun toString(): String {
            return "5"
        }
    },
    LEVEL6{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*")
        override var maxNumb = 80
        override fun toString(): String {
            return "6"
        }
    },
    LEVEL7{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*", "/")
        override var maxNumb = 80
        override fun toString(): String {
            return "7"
        }
    },
    LEVEL8{
        override var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*", "/")
        override var maxNumb = 160
        override fun toString(): String {
            return "8"
        }
    };

    open var operations : ArrayList<String> = ArrayList()
    open var maxNumb = 0

    fun getNextLevel(lvl : Levels) : Levels{
        if(lvl == LEVEL1){
            return LEVEL2
        }
        if (lvl == LEVEL2){
            return LEVEL3
        }
        if (lvl == LEVEL3){
            return LEVEL4
        }
        if (lvl == LEVEL4){
            return LEVEL5
        }
        if (lvl == LEVEL5){
            return LEVEL6
        }
        if (lvl == LEVEL6){
            return LEVEL7
        }

        return LEVEL8
    }
}